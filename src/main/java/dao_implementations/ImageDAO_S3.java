package dao_implementations;

import dao_interfaces.ImageDAO;
import models.Accomodation;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import utils.AuthenticationResult;

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
public class ImageDAO_S3 extends RestDAO implements ImageDAO {
    private static final String REPOSITORY = "s3";

    @Override
    public boolean deleteAllImages(Accomodation accomodation) throws IOException, InterruptedException {
        if (accomodation.getImages() == null)
            return false;

        for (String imageS3Url : accomodation.getImages())
            if (!delete(imageS3Url))
                return false;

        return true;
    }

    @Override
    public boolean delete(String imageS3Url) throws IOException, InterruptedException {
        String URL = getBaseUrl();
        URL = URL.concat("/" + REPOSITORY);
        URL = URL.concat("/delete-file?");
        URL = URL.concat("url=" + imageS3Url);

        AuthenticationResult authenticationResult = AuthenticationResult.getInstance();

        HttpClient httpClient = getHttpClient();
        HttpRequest httpRequest = HttpRequest
                .newBuilder()
                .DELETE()
                .uri(URI.create(URL))
                .header("Content-Type", "application/json")
                .header("Authorization", authenticationResult.getTokenType() + " " + authenticationResult.getIdToken())
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest,
                HttpResponse.BodyHandlers.ofString());

        System.out.println("delete: " + response.headers() + " " + response.body()); // dbg

        return response.statusCode() == 200;
    }

    @Override
    public String load(File file) throws IOException {
        final String URL = "http://troppadvisorserver-env.eba-pfsmp3kx.us-east-1.elasticbeanstalk.com/s3/upload-file";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        final HttpPost request = new HttpPost(URL);
        HttpEntity entity = MultipartEntityBuilder.create()
                .addPart("file", new FileBody(file))
                .build();
        request.setEntity(entity);
        CloseableHttpResponse response = httpClient.execute(request);
        StatusLine statusLine = response.getStatusLine();
        //System.out.println(statusLine.getStatusCode() + " " + statusLine.getReasonPhrase()); // dbg
        if (statusLine.getStatusCode() == 200)
            return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8); // response.body
        else
            throw new RuntimeException(String.valueOf(statusLine.getStatusCode()));
    }
}
