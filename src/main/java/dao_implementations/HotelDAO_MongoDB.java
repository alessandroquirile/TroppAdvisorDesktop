package dao_implementations;

import com.fasterxml.jackson.databind.ObjectMapper;
import controllers_utils.ObjectMapperCreator;
import dao_interfaces.CityDAO;
import dao_interfaces.HotelDAO;
import dao_interfaces.ImageDAO;
import factories.DAOFactory;
import models.Accomodation;
import models.Hotel;
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
public class HotelDAO_MongoDB extends RestDAO implements HotelDAO {
    private DAOFactory daoFactory;
    private ImageDAO imageDAO;
    private CityDAO cityDAO;
    private AuthenticationResult authenticationResult;
    private static final String REPOSITORY = "hotel";

    @Override
    public boolean add(Hotel hotel) throws IOException, InterruptedException {
        String URL = getBaseUrl();
        URL = URL.concat("/" + REPOSITORY);
        URL = URL.concat("/insert?");
        URL = URL.concat("latitude=" + hotel.getLatitude());
        URL = URL.concat("&longitude=" + hotel.getLongitude());

        final Map<String, Object> values = new HashMap<>();
        values.put("name", hotel.getName());
        values.put("stars", hotel.getStars());
        values.put("avarageRating", hotel.getAvarageRating());
        values.put("avaragePrice", hotel.getAvaragePrice());
        values.put("phoneNumber", hotel.getPhoneNumber());
        values.put("address", hotel.getAddress());
        values.put("point", hotel.getPoint());

        ObjectMapper objectMapper = ObjectMapperCreator.getNewObjectMapper();
        String requestBody = objectMapper.writeValueAsString(values);

        authenticationResult = AuthenticationResult.getInstance();

        HttpClient httpClient = getHttpClient();
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

        if (!hotel.getImages().isEmpty()) {
            for (String imagePath : hotel.getImages()) {
                File file = new File(imagePath);
                daoFactory = DAOFactory.getInstance();
                imageDAO = daoFactory.getImageDAO(ConfigFileReader.getProperty("image_storage_technology"));
                String endpoint = imageDAO.load(file);
                Hotel parsedHotel = (Hotel) getParsedAccomodationFromJson(objectMapper, response, this);
                if (!updateImage(parsedHotel, endpoint))
                    return false;
            }
            cityDAO = daoFactory.getCityDAO(ConfigFileReader.getProperty("city_storage_technology"));
            return cityDAO.insert((Accomodation) getParsedAccomodationFromJson(objectMapper, response, this));
        }
        return true;
    }

    @Override
    public List<Hotel> retrieveAt(int page, int size) throws IOException, InterruptedException {
        String URL = getBaseUrl();
        URL = URL.concat("/" + REPOSITORY);
        URL = URL.concat("/find-all?");
        URL = URL.concat("page=" + page);
        URL = URL.concat("&size=" + size);

        authenticationResult = AuthenticationResult.getInstance();

        HttpClient httpClient = getHttpClient();
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
        List<Hotel> hotels = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            ObjectMapper objectMapper = ObjectMapperCreator.getNewObjectMapper();
            Hotel hotel = objectMapper.readValue(jsonArray.get(i).toString(), Hotel.class);
            hotels.add(hotel);
        }

        return hotels;
    }

    @Override
    public List<Hotel> retrieveAt(String query, int page, int size) throws IOException, InterruptedException {
        String URL = getBaseUrl();
        URL = URL.concat("/" + REPOSITORY);
        URL = URL.concat("/search-by-rsql-no-point?");

        if (query != null)
            URL += "query=" + URLEncoder.encode(query, StandardCharsets.UTF_8) + "&page=" + page + "&size=" + size;
        else
            URL += "page=" + page + "&size=" + size;

        authenticationResult = AuthenticationResult.getInstance();

        HttpClient httpClient = getHttpClient();
        HttpRequest httpRequest = HttpRequest
                .newBuilder()
                .GET()
                .header("accept", "application/json")
                .header("Authorization", authenticationResult.getTokenType() + " " + authenticationResult.getIdToken())
                .uri(URI.create(URL))
                .build();

        HttpResponse<String> httpResponses = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        List<Hotel> hotels = null;

        if (httpResponses.statusCode() == 204) // No content
            return null;
        else if (httpResponses.statusCode() == 200) {
            JSONObject jsonObject = new JSONObject(httpResponses.body());
            JSONArray jsonArray = jsonObject.getJSONArray("content");
            hotels = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                ObjectMapper objectMapper = ObjectMapperCreator.getNewObjectMapper();
                Hotel hotel = objectMapper.readValue(jsonArray.get(i).toString(), Hotel.class);
                hotels.add(hotel);
            }
        }

        return hotels;
    }

    @Override
    public boolean delete(Hotel hotel) throws IOException, InterruptedException {
        daoFactory = DAOFactory.getInstance();
        imageDAO = daoFactory.getImageDAO(ConfigFileReader.getProperty("image_storage_technology"));
        cityDAO = daoFactory.getCityDAO(ConfigFileReader.getProperty("city_storage_technology"));

        if (!cityDAO.delete(hotel) || !imageDAO.deleteAllImages(hotel))
            return false;

        String URL = getBaseUrl();
        URL = URL.concat("/" + REPOSITORY);
        URL = URL.concat("/delete-by-id");
        URL = URL.concat("/" + hotel.getId());


        HttpClient httpClient = getHttpClient();

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
    public boolean update(Hotel hotel) throws IOException, InterruptedException {
        String URL = getBaseUrl();
        URL = URL.concat("/" + REPOSITORY);
        URL = URL.concat("/update");

        final Map<String, Object> values = new HashMap<>();
        values.put("id", hotel.getId());
        values.put("name", hotel.getName());
        values.put("stars", hotel.getStars());
        values.put("avarageRating", hotel.getAvarageRating());
        values.put("avaragePrice", hotel.getAvaragePrice());
        values.put("phoneNumber", hotel.getPhoneNumber());
        values.put("address", hotel.getAddress());
        values.put("point", hotel.getPoint());
        values.put("certificateOfExcellence", hotel.isHasCertificateOfExcellence());

        ObjectMapper objectMapper = ObjectMapperCreator.getNewObjectMapper();
        String requestBody = objectMapper.writeValueAsString(values);

        authenticationResult = AuthenticationResult.getInstance();

        HttpClient httpClient = getHttpClient();
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
    public boolean deleteImage(Hotel hotel, String imageUrl) throws IOException, InterruptedException {
        String URL = getBaseUrl();
        URL = URL.concat("/" + REPOSITORY);
        URL = URL.concat("/delete-image");

        final Map<String, Object> values = new HashMap<>();
        values.put("url", imageUrl);

        ObjectMapper objectMapper = ObjectMapperCreator.getNewObjectMapper();
        String requestBody = objectMapper.writeValueAsString(values);

        authenticationResult = AuthenticationResult.getInstance();

        HttpClient httpClient = getHttpClient();
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
    public boolean updateImage(Hotel hotel, String endpoint) throws IOException, InterruptedException {
        String URL = getBaseUrl();
        URL = URL.concat("/" + REPOSITORY);
        URL = URL.concat("/update-images");
        URL = URL.concat("/" + hotel.getId());

        final Map<String, Object> values = new HashMap<>();
        values.put("url", endpoint);

        ObjectMapper objectMapper = ObjectMapperCreator.getNewObjectMapper();
        String requestBody = objectMapper.writeValueAsString(values);

        authenticationResult = AuthenticationResult.getInstance();

        HttpClient httpClient = getHttpClient();
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
