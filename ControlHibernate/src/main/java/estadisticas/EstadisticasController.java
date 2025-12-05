package estadisticas;

import core.dao.FichajeDAO;
import core.model.Fichaje;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;

import java.time.Duration;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class EstadisticasController {

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    private FichajeDAO fichajeDAO = new FichajeDAO();

    @FXML
    private void initialize() {

        barChart.getData().clear();

        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(480);
        yAxis.setTickUnit(60);

        List<Fichaje> fichajes = fichajeDAO.findAll();

        Map<String, List<Fichaje>> fichajesPorEmpleado = fichajes.stream()
                .collect(Collectors.groupingBy(f -> f.getTrabajador().getNombre()));

        for (String empleado : fichajesPorEmpleado.keySet()) {

            XYChart.Series<String, Number> serie = new XYChart.Series<>();
            serie.setName(empleado);

            List<Fichaje> listaEmpleado = fichajesPorEmpleado.get(empleado);

            Map<LocalDate, List<Fichaje>> fichajesPorDia =
                    listaEmpleado.stream()
                            .collect(Collectors.groupingBy(Fichaje::getFecha));

            List<LocalDate> fechasOrdenadas = new ArrayList<>(fichajesPorDia.keySet());
            fechasOrdenadas.sort(Comparator.naturalOrder());

            for (LocalDate fecha : fechasOrdenadas) {
                List<Fichaje> fichajesDelDia = fichajesPorDia.get(fecha);

                Fichaje entrada = fichajesDelDia.stream()
                        .filter(f -> f.getTipo() == Fichaje.Tipo.ENTRADA)
                        .findFirst().orElse(null);

                Fichaje salida = fichajesDelDia.stream()
                        .filter(f -> f.getTipo() == Fichaje.Tipo.SALIDA)
                        .findFirst().orElse(null);

                if (entrada != null && salida != null) {

                    long minutos = Duration.between(entrada.getHora(), salida.getHora()).toMinutes();

                    XYChart.Data<String, Number> dataPoint =
                            new XYChart.Data<>(fecha.toString(), minutos);

                    serie.getData().add(dataPoint);

                    String tooltipText =
                            empleado + "\n" +
                                    fecha + "\n" +
                                    String.format("%d minutos", minutos);

                    dataPoint.nodeProperty().addListener((obs, o, node) -> {
                        if (node != null) {
                            Tooltip.install(node, new Tooltip(tooltipText));
                        }
                    });
                }
            }

            barChart.getData().add(serie);
        }
    }
}

