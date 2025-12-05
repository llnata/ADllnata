package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Pos;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);

        javafx.scene.control.Label lblTitulo = new javafx.scene.control.Label("Página Principal");
        lblTitulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Button btnFichar = new Button("Fichar");
        Button btnGestion = new Button("Gestión");
        Button btnEstadisticas = new Button("Estadísticas");
        Button btnSalir = new Button("Finalizar");

        btnFichar.setPrefWidth(200);
        btnGestion.setPrefWidth(200);
        btnEstadisticas.setPrefWidth(200);
        btnSalir.setPrefWidth(200);

        btnFichar.setStyle("-fx-font-size: 16px;");
        btnGestion.setStyle("-fx-font-size: 16px;");
        btnEstadisticas.setStyle("-fx-font-size: 16px;");
        btnSalir.setStyle("-fx-font-size: 16px; -fx-background-color: #ff5555; -fx-text-fill: white;");

        btnFichar.setOnAction(e -> abrirFXML("/fichar.fxml"));
        btnGestion.setOnAction(e -> abrirFXML("/gestion.fxml"));
        btnEstadisticas.setOnAction(e -> abrirFXML("/estadisticas.fxml"));
        btnSalir.setOnAction(e -> stage.close());

        root.getChildren().addAll(lblTitulo, btnFichar, btnGestion, btnEstadisticas, btnSalir);

        stage.setScene(new Scene(root, 400, 400));
        stage.setTitle("ControlHibernate - Menú Principal");
        stage.show();
    }

    private void abrirFXML(String rutaFXML) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFXML));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("ControlHibernate");
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}



