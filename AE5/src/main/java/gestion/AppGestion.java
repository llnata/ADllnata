package gestion;
import core.dao.FichajeDAO;
import core.model.Fichaje;
import core.service.FichajeService;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


public class AppGestion extends Application {


    @Override
    public void start(Stage stage) {
        FichajeDAO dao = new FichajeDAO();
        FichajeService service = new FichajeService();


        TableView<Fichaje> tabla = new TableView<>();
        ObservableList<Fichaje> datos = FXCollections.observableArrayList(dao.findAll());
        tabla.setItems(datos);


        TableColumn<Fichaje, String> colTrabajador = new TableColumn<>("Trabajador");
        colTrabajador.setCellValueFactory(c -> javafx.beans.binding.Bindings.createStringBinding(
                () -> c.getValue().getTrabajador().getNombre()));


        TableColumn<Fichaje, LocalDate> colFecha = new TableColumn<>("Fecha");
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));


        TableColumn<Fichaje, String> colHora = new TableColumn<>("Hora");
        colHora.setCellValueFactory(new PropertyValueFactory<>("hora"));


        TableColumn<Fichaje, String> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));


        TableColumn<Fichaje, String> colClima = new TableColumn<>("Clima");
        colClima.setCellValueFactory(new PropertyValueFactory<>("clima"));


        TableColumn<Fichaje, String> colTotalDia = new TableColumn<>("Total Día (min)");
        colTotalDia.setCellValueFactory(c -> {
            Fichaje fichaje = c.getValue();
            LocalDate fecha = fichaje.getFecha();
            int trabajadorId = fichaje.getTrabajador().getId();


            List<Fichaje> delDia = datos.stream()
                    .filter(f -> f.getFecha().equals(fecha) && f.getTrabajador().getId() == trabajadorId)
                    .collect(Collectors.toList());


            int minutos = service.calcularMinutosDia(delDia);
            return javafx.beans.binding.Bindings.createStringBinding(() -> minutos + "");
        });


        tabla.getColumns().addAll(colTrabajador, colFecha, colHora, colTipo, colClima, colTotalDia);


        VBox root = new VBox(tabla);
        stage.setScene(new Scene(root, 800, 400));
        stage.setTitle("Gestión de Fichajes");
        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }
}