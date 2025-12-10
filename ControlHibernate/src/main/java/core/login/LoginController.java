package core.login;

import core.Sesion;
import core.dao.TrabajadorDAO;
import core.model.Trabajador;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.MainApp;

public class LoginController {

    @FXML
    private TextField tfDni;

    @FXML
    private Label lblMensaje;

    private final TrabajadorDAO trabajadorDAO = new TrabajadorDAO();

    @FXML
    private void onEntrar() {
        String dni = tfDni.getText().trim();

        if (dni.isEmpty()) {
            lblMensaje.setText("Introduce el DNI");
            return;
        }

        Trabajador t = trabajadorDAO.findByDni(dni);

        if (t == null) {
            lblMensaje.setText("DNI no encontrado");
            return;
        }

        // guardar en sesión
        Sesion.setUsuarioActual(t);

        // cerrar ventana de login
        Stage loginStage = (Stage) tfDni.getScene().getWindow();
        loginStage.close();

        // abrir la aplicación principal
        try {
            new MainApp().start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

