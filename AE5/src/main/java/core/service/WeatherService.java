package core.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

public class WeatherService {

    private static final String API_KEY = "9dbe1e8df48145f4219b56d3ebf8d460";

    public static String getWeatherForCity(String city) {
        try {
            String url = "https://api.openweathermap.org/data/2.5/weather?q="
                    + city + "&appid=" + API_KEY + "&units=metric&lang=es";

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JsonObject obj = JsonParser.parseString(response.body()).getAsJsonObject();
            JsonArray weatherArr = obj.getAsJsonArray("weather");
            String description = weatherArr.get(0).getAsJsonObject().get("description").getAsString();
            double temp = obj.getAsJsonObject("main").get("temp").getAsDouble();

            return description + ", " + temp + " °C";

        } catch (Exception e) {
            e.printStackTrace();
            return "no disponible";
        }
    }
}