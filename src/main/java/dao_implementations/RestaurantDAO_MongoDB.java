package dao_implementations;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao_interfaces.RestaurantDAO;
import models.Restaurant;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

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
public class RestaurantDAO_MongoDB implements RestaurantDAO {
    @Override
    public boolean add(Restaurant restaurant) throws IOException, InterruptedException {
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

        ObjectMapper objectMapper = getNewObjectMapper();
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
            for (String imagePath : restaurant.getImages()) {
                File file = new File(imagePath);
                String endpoint = loadImageOnS3(file);
                Restaurant parsedRestaurant = getParsedRestaurantFromJson(objectMapper, response);
                if (!updateRestaurantImage(parsedRestaurant, endpoint))
                    return false;
            }
            return insertRestaurantIntoCity(getParsedRestaurantFromJson(objectMapper, response));
        } else {
            return false;
        }
    }

    @Override
    public List<Restaurant> retrieveAt(int page, int size) throws IOException, InterruptedException {
        String URL = "http://Troppadvisorserver-env.eba-pfsmp3kx.us-east-1.elasticbeanstalk.com/restaurant/find-all/?";
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
        List<Restaurant> restaurants = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            ObjectMapper objectMapper = getNewObjectMapper();
            Restaurant restaurant = objectMapper.readValue(jsonArray.get(i).toString(), Restaurant.class);
            restaurants.add(restaurant);
        }

        // dbg
        /*for (Restaurant restaurant : restaurants) {
            System.out.println(restaurant.getName());
        }*/
        return restaurants;
    }

    @Override
    public boolean delete(Restaurant restaurant) throws IOException, InterruptedException {
        // codice per eliminare un ristorante su mongodb
        String URL = "http://Troppadvisorserver-env.eba-pfsmp3kx.us-east-1.elasticbeanstalk.com/restaurant/delete-by-id";
        URL += "/" + restaurant.getId();

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
        // TODO: Eliminare le foto
        return response.statusCode() == 200;
    }

    @Override
    public boolean update(Restaurant restaurant) throws IOException, InterruptedException {
        // codice per aggiornare un ristorante su mongodb
        final String URL = "http://Troppadvisorserver-env.eba-pfsmp3kx.us-east-1.elasticbeanstalk.com/restaurant/update";

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

        // TODO: aggiornare foto e city?

        ObjectMapper objectMapper = getNewObjectMapper();
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

        //System.out.println(restaurant + "\n");
        //System.out.println(response.body() + "\n" + response.headers().toString()); // dbg

        return response.statusCode() == 200;
    }

    private String deleteImagesFromS3(File image) {
        return null;
    }

    private boolean insertRestaurantIntoCity(Restaurant restaurant) throws IOException, InterruptedException {
        String URL = "http://Troppadvisorserver-env.eba-pfsmp3kx.us-east-1.elasticbeanstalk.com/city/" +
                "insert-city-restaurant?";

        // restaurant.getCity() NON viola la Legge di Demetra
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

    private String getUrlInsertFor(Restaurant restaurant) {
        String URL = "http://Troppadvisorserver-env.eba-pfsmp3kx.us-east-1.elasticbeanstalk.com/restaurant/insert?";
        URL = URL.concat("latitude=" + restaurant.getPoint().getX());
        URL = URL.concat("&longitude=" + restaurant.getPoint().getY());
        return URL;
    }

    private ObjectMapper getNewObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

    private String loadImageOnS3(File image) throws IOException {

        final String URL = "http://troppadvisorserver-env.eba-pfsmp3kx.us-east-1.elasticbeanstalk.com/s3/upload-file";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        final HttpPost request = new HttpPost(URL);
        HttpEntity entity = MultipartEntityBuilder.create()
                .addPart("file", new FileBody(image))
                .build();
        request.setEntity(entity);
        CloseableHttpResponse response = httpClient.execute(request);
        //StatusLine statusLine = response.getStatusLine();
        // System.out.println(statusLine.getStatusCode() + " " + statusLine.getReasonPhrase());
        return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8); // response.body
    }

    private Restaurant getParsedRestaurantFromJson(ObjectMapper objectMapper, HttpResponse<String> response) throws IOException {
        return objectMapper.readValue(response.body(), new TypeReference<Restaurant>() {
        });
    }

    private boolean updateRestaurantImage(Restaurant restaurant, String endpoint) throws IOException, InterruptedException {
        String URL = "http://Troppadvisorserver-env.eba-pfsmp3kx.us-east-1.elasticbeanstalk.com/restaurant/update-images";
        URL += "/" + restaurant.getId();

        final Map<String, Object> values = new HashMap<>();
        values.put("url", endpoint);

        ObjectMapper objectMapper = getNewObjectMapper();
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

        return response.statusCode() == 200;
    }
}
