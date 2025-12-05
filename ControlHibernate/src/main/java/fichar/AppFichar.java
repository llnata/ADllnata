package fichar;


import core.dao.FichajeDAO;
import core.dao.TrabajadorDAO;
import core.model.Fichaje;
import core.model.Trabajador;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import java.time.LocalDate;
import java.time.LocalTime;


public class AppFichar extends Application {


    @Override
    public void start(Stage stage) {
        TrabajadorDAO trabajadorDAO = new TrabajadorDAO();
        FichajeDAO fichajeDAO = new FichajeDAO();


        ComboBox<Trabajador> combo = new ComboBox<>();
        combo.getItems().addAll(trabajadorDAO.findAll());


        Button btnEntrada = new Button("Fichar Entrada");
        Button btnSalida = new Button("Fichar Salida");


        Label info = new Label();


        btnEntrada.setOnAction(e -> {
            Trabajador t = combo.getValue();
            if (t == null) return;
            fichajeDAO.save(new Fichaje(t, LocalDate.now(), LocalTime.now(), Fichaje.Tipo.ENTRADA, "soleado"));
            info.setText("Entrada registrada");
        });


        btnSalida.setOnAction(e -> {
            Trabajador t = combo.getValue();
            if (t == null) return;
            fichajeDAO.save(new Fichaje(t, LocalDate.now(), LocalTime.now(), Fichaje.Tipo.SALIDA, "soleado"));
            info.setText("Salida registrada");
        });


        VBox root = new VBox(10, combo, btnEntrada, btnSalida, info);
        stage.setScene(new Scene(root, 300, 200));
        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }
}
