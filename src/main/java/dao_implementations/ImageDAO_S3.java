package dao_implementations;

import com.fasterxml.jackson.databind.ObjectMapper;
import controllers_utils.ObjectMapperCreator;
import dao_interfaces.ImageDAO;
import models.Restaurant;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public class ImageDAO_S3 implements ImageDAO {
    @Override
    public boolean deleteAllImagesFromRestaurant(Restaurant restaurant) throws IOException, InterruptedException {
        for (String imageS3Url : restaurant.getImages()) {
            if (!deleteThisImage(imageS3Url))
                return false;
        }
        return true;
    }

    @Override
    public boolean deleteThisImage(String imageS3Url) throws IOException, InterruptedException {
        String URL = "http://troppadvisorserver-env.eba-pfsmp3kx.us-east-1.elasticbeanstalk.com/s3/delete-file/?";
        URL += "url=" + imageS3Url;

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest
                .newBuilder()
                .DELETE()
                .uri(URI.create(URL))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest,
                HttpResponse.BodyHandlers.ofString());

        // System.out.println(response.body()); // dbg

        return response.statusCode() == 200;
    }

    @Override
    public String loadFile(File file) throws IOException {
        final String URL = "http://troppadvisorserver-env.eba-pfsmp3kx.us-east-1.elasticbeanstalk.com/s3/upload-file";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        final HttpPost request = new HttpPost(URL);
        HttpEntity entity = MultipartEntityBuilder.create()
                .addPart("file", new FileBody(file))
                .build();
        request.setEntity(entity);
        CloseableHttpResponse response = httpClient.execute(request);
        //StatusLine statusLine = response.getStatusLine();
        // System.out.println(statusLine.getStatusCode() + " " + statusLine.getReasonPhrase());
        return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8); // response.body
    }

    @Override
    public boolean updateRestaurantImage(Restaurant restaurant, String endpoint) throws IOException, InterruptedException {
        String URL = "http://Troppadvisorserver-env.eba-pfsmp3kx.us-east-1.elasticbeanstalk.com/restaurant/update-images";
        URL += "/" + restaurant.getId();

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

        return response.statusCode() == 200;
    }
}
