// gestion/GestionController.java
package gestion;

import core.Sesion;
import core.dao.FichajeDAO;
import core.model.Fichaje;
import core.model.Trabajador;
import core.service.FichajeService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class GestionController {

    public static class FilaDia {
        private Trabajador trabajador;
        private LocalDate fecha;
        private String horario;
        private String clima;
        private int minutos;

        public FilaDia(Trabajador trabajador, LocalDate fecha,
                       String horario, String clima, int minutos) {
            this.trabajador = trabajador;
            this.fecha = fecha;
            this.horario = horario;
            this.clima = clima;
            this.minutos = minutos;
        }

        public Trabajador getTrabajador() { return trabajador; }
        public LocalDate getFecha() { return fecha; }
        public String getHorario() { return horario; }
        public String getClima() { return clima; }
        public int getMinutos() { return minutos; }

        public void setClima(String clima) { this.clima = clima; }
        public void setMinutos(int minutos) { this.minutos = minutos; }
    }

    @FXML private TableView<FilaDia> tablaFichajes;
    @FXML private TableColumn<FilaDia, String> colTrabajador;
    @FXML private TableColumn<FilaDia, String> colFecha;
    @FXML private TableColumn<FilaDia, String> colHora;      // rango horario
    @FXML private TableColumn<FilaDia, String> colClima;
    @FXML private TableColumn<FilaDia, String> colTotalDia;

    private FichajeDAO dao = new FichajeDAO();
    private FichajeService service = new FichajeService();

    private List<FilaDia> filas; // lo que se muestra y se exporta

    @FXML
    private void initialize() {

        Trabajador actual = Sesion.getUsuarioActual();
        boolean esAdmin = actual != null && actual.getRol() == Trabajador.Rol.ADMIN;

        List<Fichaje> todos = dao.findAll();

        // Si NO es ADMIN, solo los suyos
        if (!esAdmin && actual != null) {
            todos = todos.stream()
                    .filter(f -> f.getTrabajador().getId() == actual.getId())
                    .collect(Collectors.toList());
        }

        // Agrupar por trabajador+fecha
        Map<String, List<Fichaje>> grupos = todos.stream()
                .collect(Collectors.groupingBy(f ->
                        f.getTrabajador().getId() + "#" + f.getFecha()));

        filas = new ArrayList<>();

        for (List<Fichaje> delGrupo : grupos.values()) {
            if (delGrupo.isEmpty()) continue;

            Trabajador t = delGrupo.get(0).getTrabajador();
            LocalDate fecha = delGrupo.get(0).getFecha();

            Fichaje entrada = delGrupo.stream()
                    .filter(d -> d.getTipo() == Fichaje.Tipo.ENTRADA)
                    .sorted(Comparator.comparing(Fichaje::getHora))
                    .findFirst().orElse(null);

            Fichaje salida = delGrupo.stream()
                    .filter(d -> d.getTipo() == Fichaje.Tipo.SALIDA)
                    .sorted(Comparator.comparing(Fichaje::getHora))
                    .findFirst().orElse(null);

            String horario;
            if (entrada != null && salida != null) {
                horario = entrada.getHora() + " - " + salida.getHora();
            } else if (entrada != null) {
                horario = entrada.getHora() + " - ?";
            } else if (salida != null) {
                horario = "? - " + salida.getHora();
            } else {
                horario = "";
            }

            // clima: por simplicidad cogemos el del primer fichaje del día
            String clima = delGrupo.get(0).getClima();

            int minutos = service.calcularMinutosDia(delGrupo);

            filas.add(new FilaDia(t, fecha, horario, clima, minutos));
        }

        // ordenar por fecha
        filas.sort(Comparator.comparing(FilaDia::getFecha));

        tablaFichajes.setItems(FXCollections.observableArrayList(filas));

        colTrabajador.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getTrabajador().getNombre()));
        colFecha.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getFecha().toString()));
        colHora.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getHorario()));
        colClima.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getClima()));
        colTotalDia.setCellValueFactory(c ->
                new SimpleStringProperty(String.valueOf(c.getValue().getMinutos())));

        // Edición solo para ADMIN
        tablaFichajes.setEditable(esAdmin);

        if (esAdmin) {
            // Clima editable
            colClima.setCellFactory(TextFieldTableCell.forTableColumn());
            colClima.setOnEditCommit(ev -> {
                FilaDia fila = ev.getRowValue();
                fila.setClima(ev.getNewValue());
                tablaFichajes.refresh();
            });

            // Total minutos editable
            colTotalDia.setCellFactory(TextFieldTableCell.forTableColumn());
            colTotalDia.setOnEditCommit(ev -> {
                FilaDia fila = ev.getRowValue();
                try {
                    int nuevosMin = Integer.parseInt(ev.getNewValue());
                    fila.setMinutos(nuevosMin);
                    tablaFichajes.refresh();
                } catch (NumberFormatException ignored) {
                    // si escribe algo que no es número, se ignora
                }
            });
        }
    }

    @FXML
    private void onExportarCsv() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Guardar CSV");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("CSV (*.csv)", "*.csv"));
            fileChooser.setInitialFileName("fichajes_resumen.csv");

            File file = fileChooser.showSaveDialog(
                    tablaFichajes.getScene().getWindow());

            if (file == null) return;

            try (PrintWriter pw = new PrintWriter(file, StandardCharsets.UTF_8)) {
                pw.println("Trabajador;Fecha;Horario;Clima;TotalDiaMin");

                for (FilaDia fila : filas) {
                    pw.printf("%s;%s;%s;%s;%d%n",
                            fila.getTrabajador().getNombre(),
                            fila.getFecha(),
                            fila.getHorario(),
                            fila.getClima(),
                            fila.getMinutos());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
