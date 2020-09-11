package dao_implementations;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import controllers_utils.ObjectMapperCreator;
import dao_interfaces.AttractionDAO;
import dao_interfaces.CityDAO;
import dao_interfaces.ImageDAO;
import factories.DAOFactory;
import models.Attraction;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.ConfigFileReader;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public class AttractionDAO_MongoDB implements AttractionDAO {
    private DAOFactory daoFactory;
    private ImageDAO imageDAO;
    private CityDAO cityDAO;

    @Override
    public boolean add(Attraction attraction) throws IOException, InterruptedException {
        final String URL = getUrlInsertFor(attraction);

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

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create(URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            for (String imagePath : attraction.getImages()) {
                File file = new File(imagePath);
                daoFactory = DAOFactory.getInstance();
                imageDAO = daoFactory.getImageDAO(ConfigFileReader.getProperty("image_storage_technology"));
                String endpoint = imageDAO.loadFileIntoBucket(file);
                Attraction parsedAttraction = getParsedAttractionFromJson(objectMapper, response);
                if (updateAttractionSingleImageFromAttractionCollection(parsedAttraction, endpoint))
                    return false;
            }
            cityDAO = daoFactory.getCityDAO(ConfigFileReader.getProperty("city_storage_technology"));
            return cityDAO.insert(getParsedAttractionFromJson(objectMapper, response));
        } else {
            return false;
        }
    }

    @Override
    public List<Attraction> retrieveAt(int page, int size) throws IOException, InterruptedException {
        String URL = "http://Troppadvisorserver-env.eba-pfsmp3kx.us-east-1.elasticbeanstalk.com/attraction/find-all/?";
        URL += "page=" + page + "&size=" + size;

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest
                .newBuilder()
                .GET()
                .header("accept", "application/json")
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
    public boolean delete(Attraction attraction) throws IOException, InterruptedException {
        daoFactory = DAOFactory.getInstance();
        imageDAO = daoFactory.getImageDAO(ConfigFileReader.getProperty("image_storage_technology"));
        cityDAO = daoFactory.getCityDAO(ConfigFileReader.getProperty("city_storage_technology"));
        if (!cityDAO.delete(attraction) || !imageDAO.deleteAllAccomodationImagesFromBucket(attraction))
            return false;
        else {
            String URL = "http://Troppadvisorserver-env.eba-pfsmp3kx.us-east-1.elasticbeanstalk.com/attraction/delete-by-id";
            URL += "/" + attraction.getId();
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest
                    .newBuilder()
                    .DELETE()
                    .uri(URI.create(URL))
                    .header("Content-Type", "application/json")
                    .build();

            HttpResponse<String> response = httpClient.send(httpRequest,
                    HttpResponse.BodyHandlers.ofString());

            System.out.println("delete (attraction) response: " + response);

            return response.statusCode() == 200;
        }
    }

    @Override
    public boolean update(Attraction attraction) throws IOException, InterruptedException {
        final String URL = "http://Troppadvisorserver-env.eba-pfsmp3kx.us-east-1.elasticbeanstalk.com/attraction/update";

        final Map<String, Object> values = new HashMap<>();
        values.put("id", attraction.getId());
        values.put("name", attraction.getName());
        values.put("avarageRating", attraction.getAvarageRating());
        values.put("avaragePrice", attraction.getAvaragePrice());
        values.put("phoneNumber", attraction.getPhoneNumber());
        values.put("address", attraction.getAddress());
        values.put("point", attraction.getPoint());
        values.put("certificateOfExcellence", attraction.isExcellent());
        values.put("openingTime", attraction.getOpeningTime());

        ObjectMapper objectMapper = ObjectMapperCreator.getNewObjectMapper();
        String requestBody = objectMapper.writeValueAsString(values);

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create(URL))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());

        //System.out.println(attraction + "\n");
        //System.out.println(response.body() + "\n" + response.headers().toString()); // dbg

        return response.statusCode() == 200;
    }

    @Override
    public boolean deleteAttractionSingleImageFromAttractionCollection(Attraction attraction, String imageUrl) throws IOException, InterruptedException {
        String URL = "http://Troppadvisorserver-env.eba-pfsmp3kx.us-east-1.elasticbeanstalk.com/attraction/delete-image";
        URL += "/" + attraction.getId();

        System.out.println(attraction);

        final Map<String, Object> values = new HashMap<>();
        values.put("url", imageUrl);

        ObjectMapper objectMapper = ObjectMapperCreator.getNewObjectMapper();
        String requestBody = objectMapper.writeValueAsString(values);

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create(URL))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());

        System.out.println("putDeleteImage\n: " + response.body() + " " + response.headers()); // dbg

        return response.statusCode() == 200;
    }

    @Override
    public boolean updateAttractionSingleImageFromAttractionCollection(Attraction attraction, String endpoint) throws IOException, InterruptedException {
        String URL = "http://Troppadvisorserver-env.eba-pfsmp3kx.us-east-1.elasticbeanstalk.com/attraction/update-images";
        URL += "/" + attraction.getId();

        final Map<String, Object> values = new HashMap<>();
        values.put("url", endpoint);

        ObjectMapper objectMapper = ObjectMapperCreator.getNewObjectMapper();
        String requestBody = objectMapper.writeValueAsString(values);

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create(URL))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());

        //System.out.println("Update Response body: " + response.body() + " " + response.statusCode()); // dbg

        return response.statusCode() != 200;
    }

    private String getUrlInsertFor(Attraction attraction) {
        String URL = "http://Troppadvisorserver-env.eba-pfsmp3kx.us-east-1.elasticbeanstalk.com/attraction/insert?";
        URL = URL.concat("latitude=" + attraction.getPoint().getX());
        URL = URL.concat("&longitude=" + attraction.getPoint().getY());
        return URL;
    }

    private Attraction getParsedAttractionFromJson(ObjectMapper objectMapper, HttpResponse<String> response) throws IOException {
        return objectMapper.readValue(response.body(), new TypeReference<Attraction>() {
        });
    }
}
