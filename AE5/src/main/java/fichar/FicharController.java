package fichar;

import core.dao.FichajeDAO;
import core.dao.TrabajadorDAO;
import core.model.Fichaje;
import core.model.Trabajador;
import core.service.WeatherService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class FicharController {

    @FXML private TextField tfNombre;
    @FXML private TextField tfDni;
    @FXML private ComboBox<Trabajador> comboTrabajadores;
    @FXML private Label lblInfo;

    private TrabajadorDAO trabajadorDAO = new TrabajadorDAO();
    private FichajeDAO fichajeDAO = new FichajeDAO();

    @FXML
    private void initialize() {
        actualizarCombo();

        comboTrabajadores.setConverter(new StringConverter<>() {
            @Override
            public String toString(Trabajador t) {
                return t != null ? t.getNombre() : "";
            }

            @Override
            public Trabajador fromString(String string) {
                return null;
            }
        });
    }

    @FXML
    private void onAgregarTrabajador() {
        String nombre = tfNombre.getText().trim();
        String dni = tfDni.getText().trim();
        if (!nombre.isEmpty() && !dni.isEmpty()) {
            Trabajador t = new Trabajador(nombre, dni);
            trabajadorDAO.save(t);
            tfNombre.clear();
            tfDni.clear();
            actualizarCombo();
            lblInfo.setText("Trabajador añadido");
        }
    }

    @FXML
    private void onFicharEntrada() throws Exception {
        fichar(Fichaje.Tipo.ENTRADA);
    }

    @FXML
    private void onFicharSalida() throws Exception {
        fichar(Fichaje.Tipo.SALIDA);
    }

    private void fichar(Fichaje.Tipo tipo) throws Exception {
        Trabajador t = comboTrabajadores.getValue();
        if (t == null) {
            lblInfo.setText("Selecciona un trabajador");
            return;
        }

        String clima = WeatherService.getWeatherForCity("Madrid");
        LocalTime hora = LocalTime.now().withNano(0);

        fichajeDAO.save(new Fichaje(t, LocalDate.now(), hora, tipo, clima));
        lblInfo.setText(tipo + " registrado: " + hora + " | Clima: " + clima);
    }

    private void actualizarCombo() {
        List<Trabajador> lista = trabajadorDAO.findAll();
        comboTrabajadores.setItems(FXCollections.observableArrayList(lista));
    }
}

