package dao_implementations;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import controllers_utils.ObjectMapperCreator;
import dao_interfaces.CityDAO;
import dao_interfaces.HotelDAO;
import dao_interfaces.ImageDAO;
import factories.DAOFactory;
import models.Hotel;
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
public class HotelDAO_MongoDB implements HotelDAO {
    private DAOFactory daoFactory;
    private ImageDAO imageDAO;
    private CityDAO cityDAO;

    @Override
    public boolean add(Hotel hotel) throws IOException, InterruptedException {
        final String URL = getUrlInsertFor(hotel);

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
            for (String imagePath : hotel.getImages()) {
                File file = new File(imagePath);
                daoFactory = DAOFactory.getInstance();
                imageDAO = daoFactory.getImageDAO(ConfigFileReader.getProperty("image_storage_technology"));
                String endpoint = imageDAO.loadFileIntoBucket(file);
                Hotel parsedHotel = getParsedHotelFromJson(objectMapper, response);
                if (updateHotelSingleImageFromHotelCollection(parsedHotel, endpoint))
                    return false;
            }
            cityDAO = daoFactory.getCityDAO(ConfigFileReader.getProperty("city_storage_technology"));
            return cityDAO.insert(getParsedHotelFromJson(objectMapper, response));
        } else {
            return false;
        }
    }

    @Override
    public List<Hotel> retrieveAt(int page, int size) throws IOException, InterruptedException {
        String URL = "http://Troppadvisorserver-env.eba-pfsmp3kx.us-east-1.elasticbeanstalk.com/hotel/find-all/?";
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
        List<Hotel> hotels = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            ObjectMapper objectMapper = ObjectMapperCreator.getNewObjectMapper();
            Hotel hotel = objectMapper.readValue(jsonArray.get(i).toString(), Hotel.class);
            hotels.add(hotel);
        }

        // dbg
        /*for (Hotel hotel : hotels) {
            System.out.println(hotel.getName());
        }*/
        return hotels;
    }

    @Override
    public boolean delete(Hotel hotel) throws IOException, InterruptedException {
        daoFactory = DAOFactory.getInstance();
        imageDAO = daoFactory.getImageDAO(ConfigFileReader.getProperty("image_storage_technology"));
        cityDAO = daoFactory.getCityDAO(ConfigFileReader.getProperty("city_storage_technology"));
        if (!cityDAO.delete(hotel) || !imageDAO.deleteAllAccomodationImagesFromBucket(hotel))
            return false;
        else {
            String URL = "http://Troppadvisorserver-env.eba-pfsmp3kx.us-east-1.elasticbeanstalk.com/hotel/delete-by-id";
            URL += "/" + hotel.getId();
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest
                    .newBuilder()
                    .DELETE()
                    .uri(URI.create(URL))
                    .header("Content-Type", "application/json")
                    .build();

            HttpResponse<String> response = httpClient.send(httpRequest,
                    HttpResponse.BodyHandlers.ofString());

            System.out.println(response.body());

            return response.statusCode() == 200;
        }
    }

    @Override
    public boolean update(Hotel hotel) throws IOException, InterruptedException {
        final String URL = "http://Troppadvisorserver-env.eba-pfsmp3kx.us-east-1.elasticbeanstalk.com/hotel/update";

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

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create(URL))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());

        //System.out.println(hotel + "\n");
        //System.out.println(response.body() + "\n" + response.headers().toString()); // dbg

        return response.statusCode() == 200;
    }

    @Override
    public boolean deleteHotelSingleImageFromHotelCollection(Hotel hotel, String imageUrl) throws IOException, InterruptedException {
        String URL = "http://Troppadvisorserver-env.eba-pfsmp3kx.us-east-1.elasticbeanstalk.com/hotel/delete-image";
        URL += "/" + hotel.getId();

        System.out.println(hotel);

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
    public boolean updateHotelSingleImageFromHotelCollection(Hotel hotel, String endpoint) throws IOException, InterruptedException {
        String URL = "http://Troppadvisorserver-env.eba-pfsmp3kx.us-east-1.elasticbeanstalk.com/hotel/update-images";
        URL += "/" + hotel.getId();

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

    private String getUrlInsertFor(Hotel hotel) {
        String URL = "http://Troppadvisorserver-env.eba-pfsmp3kx.us-east-1.elasticbeanstalk.com/hotel/insert?";
        URL = URL.concat("latitude=" + hotel.getPoint().getX());
        URL = URL.concat("&longitude=" + hotel.getPoint().getY());
        return URL;
    }

    private Hotel getParsedHotelFromJson(ObjectMapper objectMapper, HttpResponse<String> response) throws IOException {
        return objectMapper.readValue(response.body(), new TypeReference<Hotel>() {
        });
    }
}
