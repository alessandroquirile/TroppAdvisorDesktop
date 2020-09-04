package dao_implementations;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao_interfaces.RestaurantDAO;
import models.Restaurant;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public class RestaurantDAO_MongoDB implements RestaurantDAO {
    @Override
    public boolean add(Restaurant restaurant) throws IOException, InterruptedException {
        // codice per inserire un ristorante su mongodb
        final String URL = getUrlInsertFor(restaurant);

        final Map<String, Object> values = new HashMap<>();
        values.put("name", restaurant.getName());
        values.put("avarageRating", restaurant.getAvarageRating());
        values.put("avaragePrice", restaurant.getAvaragePrice());
        values.put("phoneNumber", restaurant.getPhoneNumber());
        values.put("address", restaurant.getAddress());
        values.put("point", restaurant.getPoint());
        values.put("typeOfCuisine", restaurant.getTypeOfCuisine());
        values.put("openingTime", restaurant.getOpeningTime());

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(values);

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create(URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());
        return true;
    }

    private String getUrlInsertFor(Restaurant restaurant) {
        String URL = "http://Troppadvisorserver-env.eba-pfsmp3kx.us-east-1.elasticbeanstalk.com/restaurant/insert?";
        URL = URL.concat("latitude=" + restaurant.getPoint().getX());
        URL = URL.concat("&longitude=" + restaurant.getPoint().getY());
        return URL;
    }

    @Override
    public boolean delete(Restaurant restaurant) {
        // codice per eliminare un ristorante su mongodb
        return true;
    }

    @Override
    public boolean update(Restaurant restaurant) {
        // codice per aggiornare un ristorante su mongodb
        return true;
    }
}
