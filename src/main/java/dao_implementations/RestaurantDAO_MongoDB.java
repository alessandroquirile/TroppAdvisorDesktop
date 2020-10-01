package dao_implementations;

import com.fasterxml.jackson.databind.ObjectMapper;
import controllers_utils.ObjectMapperCreator;
import dao_interfaces.CityDAO;
import dao_interfaces.ImageDAO;
import dao_interfaces.RestaurantDAO;
import factories.DAOFactory;
import models.Accomodation;
import models.Restaurant;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.AuthenticationResult;
import utils.ConfigFileReader;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public class RestaurantDAO_MongoDB extends RestDAO implements RestaurantDAO {
    private DAOFactory daoFactory;
    private ImageDAO imageDAO;
    private CityDAO cityDAO;
    private AuthenticationResult authenticationResult;
    private static final String REPOSITORY = "restaurant";

    @Override
    public boolean add(Restaurant restaurant) throws IOException, InterruptedException {
        String URL = getBaseUrl();
        URL = URL.concat("/" + REPOSITORY);
        URL = URL.concat("/insert?");
        URL = URL.concat("latitude=" + restaurant.getLatitude());
        URL = URL.concat("&longitude=" + restaurant.getLongitude());

        final Map<String, Object> values = new HashMap<>();
        values.put("name", restaurant.getName());
        values.put("avarageRating", restaurant.getAvarageRating());
        values.put("avaragePrice", restaurant.getAvaragePrice());
        values.put("phoneNumber", restaurant.getPhoneNumber());
        values.put("address", restaurant.getAddress());
        values.put("point", restaurant.getPoint());
        values.put("typeOfCuisine", restaurant.getTypeOfCuisine());
        values.put("openingTime", restaurant.getOpeningTime());

        ObjectMapper objectMapper = ObjectMapperCreator.getNewObjectMapper();
        String requestBody = objectMapper.writeValueAsString(values);

        authenticationResult = AuthenticationResult.getInstance();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create(URL))
                .header("Content-Type", "application/json")
                .header("Authorization", authenticationResult.getTokenType() + " " + authenticationResult.getIdToken())
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200)
            return false;

        for (String imagePath : restaurant.getImages()) {
            File file = new File(imagePath);
            daoFactory = DAOFactory.getInstance();
            imageDAO = daoFactory.getImageDAO(ConfigFileReader.getProperty("image_storage_technology"));
            String endpoint = imageDAO.load(file);
            Restaurant parsedRestaurant = (Restaurant) getParsedAccomodationFromJson(objectMapper, response, this);
            if (!updateImage(parsedRestaurant, endpoint))
                return false;
        }
        cityDAO = daoFactory.getCityDAO(ConfigFileReader.getProperty("city_storage_technology"));
        return cityDAO.insert((Accomodation) getParsedAccomodationFromJson(objectMapper, response, this));
    }

    @Override
    public List<Restaurant> retrieveAt(int page, int size) throws IOException, InterruptedException {
        String URL = getBaseUrl();
        URL = URL.concat("/" + REPOSITORY);
        URL = URL.concat("/find-all?");
        URL = URL.concat("page=" + page);
        URL = URL.concat("&size=" + size);

        authenticationResult = AuthenticationResult.getInstance();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest
                .newBuilder()
                .GET()
                .header("accept", "application/json")
                .header("Authorization", authenticationResult.getTokenType() + " " + authenticationResult.getIdToken())
                .uri(URI.create(URL))
                .build();

        HttpResponse<String> httpResponses = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        JSONObject jsonObject = new JSONObject(httpResponses.body());
        JSONArray jsonArray = jsonObject.getJSONArray("content");
        List<Restaurant> restaurants = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            ObjectMapper objectMapper = ObjectMapperCreator.getNewObjectMapper();
            Restaurant restaurant = objectMapper.readValue(jsonArray.get(i).toString(), Restaurant.class);
            restaurants.add(restaurant);
        }

        return restaurants;
    }

    @Override
    public List<Restaurant> retrieveAt(String query, int page, int size) throws IOException, InterruptedException {
        String URL = getBaseUrl();
        URL = URL.concat("/" + REPOSITORY);
        URL = URL.concat("/search-by-rsql-no-point?");

        if (query != null)
            URL += "query=" + URLEncoder.encode(query, StandardCharsets.UTF_8) + "&page=" + page + "&size=" + size;
        else
            URL += "page=" + page + "&size=" + size;

        System.out.println("URL: " + URL);

        authenticationResult = AuthenticationResult.getInstance();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest
                .newBuilder()
                .GET()
                .header("accept", "application/json")
                .header("Authorization", authenticationResult.getTokenType() + " " + authenticationResult.getIdToken())
                .uri(URI.create(URL))
                .build();

        HttpResponse<String> httpResponses = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        List<Restaurant> restaurants = null;

        if (httpResponses.statusCode() == 204) // No content
            return null;
        else if (httpResponses.statusCode() == 200) {
            JSONObject jsonObject = new JSONObject(httpResponses.body());
            JSONArray jsonArray = jsonObject.getJSONArray("content");
            restaurants = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                ObjectMapper objectMapper = ObjectMapperCreator.getNewObjectMapper();
                Restaurant restaurant = objectMapper.readValue(jsonArray.get(i).toString(), Restaurant.class);
                restaurants.add(restaurant);
            }
        }

        return restaurants;
    }

    @Override
    public boolean delete(Restaurant restaurant) throws IOException, InterruptedException {
        daoFactory = DAOFactory.getInstance();
        imageDAO = daoFactory.getImageDAO(ConfigFileReader.getProperty("image_storage_technology"));
        cityDAO = daoFactory.getCityDAO(ConfigFileReader.getProperty("city_storage_technology"));

        if (!cityDAO.delete(restaurant) || !imageDAO.deleteAllImages(restaurant))
            return false;

        String URL = getBaseUrl();
        URL = URL.concat("/" + REPOSITORY);
        URL = URL.concat("/delete-by-id");
        URL = URL.concat("/" + restaurant.getId());

        HttpClient httpClient = HttpClient.newHttpClient();

        authenticationResult = AuthenticationResult.getInstance();

        HttpRequest httpRequest = HttpRequest
                .newBuilder()
                .DELETE()
                .uri(URI.create(URL))
                .header("Content-Type", "application/json")
                .header("Authorization", authenticationResult.getTokenType() + " " + authenticationResult.getIdToken())
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest,
                HttpResponse.BodyHandlers.ofString());

        return response.statusCode() == 200;
    }

    @Override
    public boolean update(Restaurant restaurant) throws IOException, InterruptedException {
        String URL = getBaseUrl();
        URL = URL.concat("/" + REPOSITORY);
        URL = URL.concat("/update");

        assert restaurant.getTypeOfCuisine() != null;
        final Map<String, Object> values = new HashMap<>();
        values.put("id", restaurant.getId());
        values.put("name", restaurant.getName());
        values.put("avarageRating", restaurant.getAvarageRating());
        values.put("avaragePrice", restaurant.getAvaragePrice());
        values.put("phoneNumber", restaurant.getPhoneNumber());
        values.put("address", restaurant.getAddress());
        values.put("point", restaurant.getPoint());
        values.put("typeOfCuisine", restaurant.getTypeOfCuisine());
        values.put("certificateOfExcellence", restaurant.isHasCertificateOfExcellence());
        values.put("openingTime", restaurant.getOpeningTime());

        ObjectMapper objectMapper = ObjectMapperCreator.getNewObjectMapper();
        String requestBody = objectMapper.writeValueAsString(values);

        authenticationResult = AuthenticationResult.getInstance();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create(URL))
                .header("Content-Type", "application/json")
                .header("Authorization", authenticationResult.getTokenType() + " " + authenticationResult.getIdToken())
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());

        return response.statusCode() == 200;
    }

    @Override
    public boolean deleteImage(Restaurant restaurant, String imageUrl) throws IOException, InterruptedException {
        String URL = getBaseUrl();
        URL = URL.concat("/" + REPOSITORY);
        URL = URL.concat("/delete-image");
        URL = URL.concat("/" + restaurant.getId());

        final Map<String, Object> values = new HashMap<>();
        values.put("url", imageUrl);

        ObjectMapper objectMapper = ObjectMapperCreator.getNewObjectMapper();
        String requestBody = objectMapper.writeValueAsString(values);

        authenticationResult = AuthenticationResult.getInstance();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create(URL))
                .header("Content-Type", "application/json")
                .header("Authorization", authenticationResult.getTokenType() + " " + authenticationResult.getIdToken())
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());

        return response.statusCode() == 200;
    }

    @Override
    public boolean updateImage(Restaurant restaurant, String imageHostUrl) throws IOException, InterruptedException {
        String URL = getBaseUrl();
        URL = URL.concat("/" + REPOSITORY);
        URL = URL.concat("/update-images");
        URL = URL.concat("/" + restaurant.getId());

        final Map<String, Object> values = new HashMap<>();
        values.put("url", imageHostUrl);

        ObjectMapper objectMapper = ObjectMapperCreator.getNewObjectMapper();
        String requestBody = objectMapper.writeValueAsString(values);

        authenticationResult = AuthenticationResult.getInstance();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create(URL))
                .header("Content-Type", "application/json")
                .header("Authorization", authenticationResult.getTokenType() + " " + authenticationResult.getIdToken())
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());

        return response.statusCode() == 200;
    }
}