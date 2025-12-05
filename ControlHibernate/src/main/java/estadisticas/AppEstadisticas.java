package estadisticas;

import core.dao.FichajeDAO;
import core.model.Fichaje;
import core.service.FichajeService;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AppEstadisticas extends Application {

    @Override
    public void start(Stage stage) {
        FichajeDAO dao = new FichajeDAO();
        FichajeService service = new FichajeService();

        List<Fichaje> todos = dao.findAll();

        Map<LocalDate, Integer> minutosPorDia = todos.stream()
                .collect(Collectors.groupingBy(Fichaje::getFecha,
                        Collectors.collectingAndThen(Collectors.toList(), list -> {
                            Map<Integer, List<Fichaje>> porTrabajador = list.stream()
                                    .collect(Collectors.groupingBy(f -> f.getTrabajador().getId()));
                            return porTrabajador.values().stream()
                                    .mapToInt(service::calcularMinutosDia).sum();
                        })));

        CategoryAxis x = new CategoryAxis();
        NumberAxis y = new NumberAxis();
        BarChart<String, Number> chart = new BarChart<>(x, y);
        chart.setTitle("Horas trabajadas por día (minutos)");
        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName("Minutos");

        minutosPorDia.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(e -> serie.getData().add(new XYChart.Data<>(e.getKey().toString(), e.getValue())));

        chart.setData(FXCollections.observableArrayList(serie));

        VBox root = new VBox(chart);
        stage.setScene(new Scene(root, 800, 450));
        stage.setTitle("Estadísticas - ControlHibernate");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
