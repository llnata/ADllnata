package core.service;

import core.model.Fichaje;
import java.time.Duration;
import java.util.List;


public class FichajeService {


    public int calcularMinutosDia(List<Fichaje> fichajes) {
        fichajes.sort((a, b) -> a.getHora().compareTo(b.getHora()));


        int minutos = 0;
        for (int i = 0; i < fichajes.size() - 1; i += 2) {
            var entrada = fichajes.get(i);
            var salida = fichajes.get(i + 1);
            minutos += Duration.between(entrada.getHora(), salida.getHora()).toMinutes();
        }
        return minutos;
    }
}