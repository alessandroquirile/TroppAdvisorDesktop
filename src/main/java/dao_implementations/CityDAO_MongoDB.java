package dao_implementations;

import dao_interfaces.CityDAO;
import models.Accomodation;
import models.Hotel;
import models.Restaurant;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public class CityDAO_MongoDB implements CityDAO {
    @Override
    public boolean insert(Accomodation accomodation) throws IOException, InterruptedException {
        if (accomodation instanceof Restaurant)
            return insertRestaurant((Restaurant) accomodation);
        else
            return insertHotel((Hotel) accomodation);
    }

    @Override
    public boolean delete(Accomodation accomodation) throws IOException, InterruptedException {
        if (accomodation instanceof Restaurant)
            return deleteRestaurant((Restaurant) accomodation);
        else
            return deleteHotel((Hotel) accomodation);
    }

    private boolean insertRestaurant(Restaurant restaurant) throws IOException, InterruptedException {
        String URL = "http://Troppadvisorserver-env.eba-pfsmp3kx.us-east-1.elasticbeanstalk.com/city/" +
                "insert-city-restaurant?";

        URL += "city=" + URLEncoder.encode(restaurant.getCity(), StandardCharsets.UTF_8) + "&nation=Italia" + "&id=" + restaurant.getId();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create(URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("")) // empty body
                .build();

        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());

        //System.out.println("Debugging: " + response.headers() + "\n" + response.body()); // dbg

        return response.statusCode() == 200;
    }

    private boolean deleteRestaurant(Restaurant restaurant) throws IOException, InterruptedException {
        String URL = "http://troppadvisorserver-env.eba-pfsmp3kx.us-east-1.elasticbeanstalk.com/city/delete-restaurant-by-id/?";
        URL += "city=" + URLEncoder.encode(restaurant.getCity(), StandardCharsets.UTF_8) + "&id=" + restaurant.getId();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest
                .newBuilder()
                .DELETE()
                .uri(URI.create(URL))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest,
                HttpResponse.BodyHandlers.ofString());

        //System.out.println(response.body()); // dbg

        return response.statusCode() == 200;
    }

    private boolean insertHotel(Hotel hotel) throws IOException, InterruptedException {
        String URL = "http://Troppadvisorserver-env.eba-pfsmp3kx.us-east-1.elasticbeanstalk.com/city/" +
                "insert-city-hotel?";

        URL += "city=" + URLEncoder.encode(hotel.getCity(), StandardCharsets.UTF_8) + "&nation=Italia" + "&id=" + hotel.getId();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create(URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("")) // empty body
                .build();

        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());

        //System.out.println("Debugging: " + response.headers() + "\n" + response.body()); // dbg

        return response.statusCode() == 200;
    }

    private boolean deleteHotel(Hotel hotel) throws IOException, InterruptedException {
        String URL = "http://troppadvisorserver-env.eba-pfsmp3kx.us-east-1.elasticbeanstalk.com/city/delete-hotel-by-id/?";
        URL += "city=" + URLEncoder.encode(hotel.getCity(), StandardCharsets.UTF_8) + "&id=" + hotel.getId();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest
                .newBuilder()
                .DELETE()
                .uri(URI.create(URL))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest,
                HttpResponse.BodyHandlers.ofString());

        //System.out.println(response.body()); // dbg

        return response.statusCode() == 200;
    }
}