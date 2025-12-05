package gestion;


import core.dao.FichajeDAO;
import core.model.Fichaje;
import core.service.FichajeService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


import java.util.List;
import java.util.stream.Collectors;


public class GestionController {


    @FXML private TableView<Fichaje> tablaFichajes;
    @FXML private TableColumn<Fichaje, String> colTrabajador;
    @FXML private TableColumn<Fichaje, String> colFecha;
    @FXML private TableColumn<Fichaje, String> colHora;
    @FXML private TableColumn<Fichaje, String> colTipo;
    @FXML private TableColumn<Fichaje, String> colClima;
    @FXML private TableColumn<Fichaje, String> colTotalDia;


    private FichajeDAO dao = new FichajeDAO();
    private FichajeService service = new FichajeService();


    @FXML
    private void initialize() {
        List<Fichaje> datos = dao.findAll();
        tablaFichajes.setItems(FXCollections.observableArrayList(datos));


        colTrabajador.setCellValueFactory(c -> javafx.beans.binding.Bindings.createStringBinding(
                () -> c.getValue().getTrabajador().getNombre()));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colHora.setCellValueFactory(new PropertyValueFactory<>("hora"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colClima.setCellValueFactory(new PropertyValueFactory<>("clima"));
        colTotalDia.setCellValueFactory(c -> javafx.beans.binding.Bindings.createStringBinding(() -> {
            Fichaje f = c.getValue();
            List<Fichaje> delDia = datos.stream()
                    .filter(d -> d.getFecha().equals(f.getFecha()) && d.getTrabajador().getId() == f.getTrabajador().getId())
                    .collect(Collectors.toList());
            return String.valueOf(service.calcularMinutosDia(delDia));
        }));
    }
}
