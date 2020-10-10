package dao_implementations;

import com.fasterxml.jackson.databind.ObjectMapper;
import controllers_utils.ObjectMapperCreator;
import dao_interfaces.AttractionDAO;
import dao_interfaces.CityDAO;
import dao_interfaces.ImageDAO;
import factories.DAOFactory;
import models.Attraction;
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
public class AttractionDAO_MongoDB extends RestDAO implements AttractionDAO {
    private DAOFactory daoFactory;
    private ImageDAO imageDAO;
    private CityDAO cityDAO;
    private AuthenticationResult authenticationResult;
    private static final String REPOSITORY = "attraction";

    @Override
    public boolean add(Attraction attraction) throws IOException, InterruptedException {
        String URL = getBaseUrl();
        URL = URL.concat("/" + REPOSITORY);
        URL = URL.concat("/insert?");
        URL = URL.concat("latitude=" + attraction.getLatitude());
        URL = URL.concat("&longitude=" + attraction.getLongitude());

        final Map<String, Object> values = new HashMap<>();
        values.put("name", attraction.getName());
        values.put("avarageRating", attraction.getAvarageRating());
        values.put("avaragePrice", attraction.getAvaragePrice());
        values.put("phoneNumber", attraction.getPhoneNumber());
        values.put("address", attraction.getAddress());
        values.put("point", attraction.getPoint());
        values.put("openingTime", attraction.getOpeningTime());

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

        for (String imagePath : attraction.getImages()) {
            File file = new File(imagePath);
            daoFactory = DAOFactory.getInstance();
            imageDAO = daoFactory.getImageDAO(ConfigFileReader.getProperty("image_storage_technology"));
            String endpoint = imageDAO.load(file);
            Attraction parsedAttraction = (Attraction) getParsedAccomodationFromJson(objectMapper, response, this);
            if (!updateImage(parsedAttraction, endpoint))
                return false;
        }
        cityDAO = daoFactory.getCityDAO(ConfigFileReader.getProperty("city_storage_technology"));
        return cityDAO.insert((Attraction) getParsedAccomodationFromJson(objectMapper, response, this));
    }

    @Override
    public List<Attraction> retrieveAt(int page, int size) throws IOException, InterruptedException {
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

        List<Attraction> attractions = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            ObjectMapper objectMapper = ObjectMapperCreator.getNewObjectMapper();
            Attraction attraction = objectMapper.readValue(jsonArray.get(i).toString(), Attraction.class);
            attractions.add(attraction);
        }

        return attractions;
    }

    @Override
    public List<Attraction> retrieveAt(String query, int page, int size) throws IOException, InterruptedException {
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

        List<Attraction> attractions = null;

        if (httpResponses.statusCode() == 204) // No content
            return null;
        else if (httpResponses.statusCode() == 200) {
            JSONObject jsonObject = new JSONObject(httpResponses.body());
            JSONArray jsonArray = jsonObject.getJSONArray("content");
            attractions = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                ObjectMapper objectMapper = ObjectMapperCreator.getNewObjectMapper();
                Attraction attraction = objectMapper.readValue(jsonArray.get(i).toString(), Attraction.class);
                attractions.add(attraction);
            }
        }

        return attractions;
    }

    @Override
    public boolean delete(Attraction attraction) throws IOException, InterruptedException {
        daoFactory = DAOFactory.getInstance();
        imageDAO = daoFactory.getImageDAO(ConfigFileReader.getProperty("image_storage_technology"));
        cityDAO = daoFactory.getCityDAO(ConfigFileReader.getProperty("city_storage_technology"));

        if (!cityDAO.delete(attraction) || !imageDAO.deleteAllImages(attraction))
            return false;

        String URL = getBaseUrl();
        URL = URL.concat("/" + REPOSITORY);
        URL = URL.concat("/delete-by-id");
        URL = URL.concat("/" + attraction.getId());

        authenticationResult = AuthenticationResult.getInstance();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest
                .newBuilder()
                .DELETE()
                .uri(URI.create(URL))
                .header("Content-Type", "application/json")
                .header("Authorization", authenticationResult.getTokenType() + " " + authenticationResult.getIdToken())
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest,
                HttpResponse.BodyHandlers.ofString());

        System.out.println("delete (attraction) response: " + response);

        return response.statusCode() == 200;
    }

    @Override
    public boolean update(Attraction attraction) throws IOException, InterruptedException {
        String URL = getBaseUrl();
        URL = URL.concat("/" + REPOSITORY);
        URL = URL.concat("/update");

        final Map<String, Object> values = new HashMap<>();
        values.put("id", attraction.getId());
        values.put("name", attraction.getName());
        values.put("avarageRating", attraction.getAvarageRating());
        values.put("avaragePrice", attraction.getAvaragePrice());
        values.put("phoneNumber", attraction.getPhoneNumber());
        values.put("address", attraction.getAddress());
        values.put("point", attraction.getPoint());
        values.put("certificateOfExcellence", attraction.isHasCertificateOfExcellence());
        values.put("openingTime", attraction.getOpeningTime());

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
    public boolean deleteImage(Attraction attraction, String imageUrl) throws IOException, InterruptedException {
        String URL = getBaseUrl();
        URL = URL.concat("/" + REPOSITORY);
        URL = URL.concat("/delete-image");
        URL = URL.concat("/" + attraction.getId());

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

        System.out.println("deleteImage: " + response.headers() + " " + response.body());

        return response.statusCode() == 200;
    }

    @Override
    public boolean updateImage(Attraction attraction, String endpoint) throws IOException, InterruptedException {
        String URL = getBaseUrl();
        URL = URL.concat("/" + REPOSITORY);
        URL = URL.concat("/update-images");
        URL = URL.concat("/" + attraction.getId());

        final Map<String, Object> values = new HashMap<>();
        values.put("url", endpoint);

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
