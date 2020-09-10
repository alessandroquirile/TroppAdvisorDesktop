package dao_implementations;

import dao_interfaces.ImageDAO;
import models.Accomodation;
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

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public class ImageDAO_S3 implements ImageDAO {
    @Override
    public boolean deleteAllAccomodationImagesFromBucket(Accomodation accomodation) throws IOException, InterruptedException {
        if (accomodation.getImages() != null) {
            for (String imageS3Url : accomodation.getImages()) {
                if (deleteThisImageFromBucket(imageS3Url))
                    return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteThisImageFromBucket(String imageS3Url) throws IOException, InterruptedException {
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

        return response.statusCode() != 200;
    }

    @Override
    public String loadFileIntoBucket(File file) throws IOException {
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
}
