package dao_implementations;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Attraction;
import models.Hotel;
import models.Restaurant;
import utils.ConfigFileReader;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public abstract class RestDAO {
    private static final String PROTOCOL = ConfigFileReader.getProperty("protocol");
    private static final String REST_API_ID = ConfigFileReader.getProperty("rest_api_id");
    private static final String REGION = ConfigFileReader.getProperty("region");
    private static final String CLOUD_HOST = ConfigFileReader.getProperty("cloud_host");
    private static final String STAGE_NAME = ConfigFileReader.getProperty("stage_name");

    protected String getBaseUrl() {
        String URL = "";
        URL = URL.concat(PROTOCOL + "://");
        URL = URL.concat(REST_API_ID + ".execute-api.");
        URL = URL.concat(REGION);
        URL = URL.concat("." + CLOUD_HOST + ".com/");
        URL = URL.concat(STAGE_NAME);
        return URL;
    }

    protected HttpClient getHttpClient() {
        return HttpClient
                .newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
    }

    protected Object getParsedAccomodationFromJson(ObjectMapper objectMapper,
                                                   HttpResponse<String> response,
                                                   RestDAO restDAO) throws IOException {
        if (restDAO instanceof AttractionDAO_MongoDB)
            return objectMapper.readValue(response.body(), new TypeReference<Attraction>() {
            });
        if (restDAO instanceof HotelDAO_MongoDB)
            return objectMapper.readValue(response.body(), new TypeReference<Hotel>() {
            });
        if (restDAO instanceof RestaurantDAO_MongoDB)
            return objectMapper.readValue(response.body(), new TypeReference<Restaurant>() {
            });
        return null;
    }
}