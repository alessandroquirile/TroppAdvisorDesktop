package dao_implementations;

import dao_interfaces.CityDAO;
import models.Accomodation;
import models.Attraction;
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
        else if (accomodation instanceof Hotel)
            return insertHotel((Hotel) accomodation);
        else if (accomodation instanceof Attraction)
            return insertAttraction((Attraction) accomodation);
        else
            throw new RuntimeException("accomodation is instance of none of the above");
    }

    @Override
    public boolean delete(Accomodation accomodation) throws IOException, InterruptedException {
        if (accomodation instanceof Restaurant)
            return deleteRestaurant((Restaurant) accomodation);
        else if (accomodation instanceof Hotel)
            return deleteHotel((Hotel) accomodation);
        else if (accomodation instanceof Attraction)
            return deleteAttraction((Attraction) accomodation);
        else
            throw new RuntimeException("accomodation is instance of none of the above");
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

        return response.statusCode() == 200;
    }

    private boolean insertAttraction(Attraction attraction) throws IOException, InterruptedException {
        String URL = "http://Troppadvisorserver-env.eba-pfsmp3kx.us-east-1.elasticbeanstalk.com/city/" +
                "insert-city-attraction?";

        URL += "city=" + URLEncoder.encode(attraction.getCity(), StandardCharsets.UTF_8) + "&nation=Italia" + "&id=" + attraction.getId();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create(URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("")) // empty body
                .build();

        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());

        return response.statusCode() == 200;
    }

    private boolean deleteAttraction(Attraction attraction) throws IOException, InterruptedException {
        String URL = "http://troppadvisorserver-env.eba-pfsmp3kx.us-east-1.elasticbeanstalk.com/city/delete-attraction-by-id/?";
        URL += "city=" + URLEncoder.encode(attraction.getCity(), StandardCharsets.UTF_8) + "&id=" + attraction.getId();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest
                .newBuilder()
                .DELETE()
                .uri(URI.create(URL))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest,
                HttpResponse.BodyHandlers.ofString());

        return response.statusCode() == 200;
    }
}