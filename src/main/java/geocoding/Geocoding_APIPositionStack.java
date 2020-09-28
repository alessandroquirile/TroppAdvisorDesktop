package geocoding;

import com.fasterxml.jackson.databind.ObjectMapper;
import controllers_utils.ObjectMapperCreator;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.GeocodingResponse;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public class Geocoding_APIPositionStack implements Geocoder {
    private static final String API_KEY = "3986e2581d9bf3b4767b73a37e478373";

    public Geocoding_APIPositionStack() {

    }

    @Override
    public GeocodingResponse forward(String address) {
        String URL = "http://api.positionstack.com/v1/forward?";
        URL += "access_key=" + API_KEY + "&query=" + URLEncoder.encode(address, StandardCharsets.UTF_8) + "&limit=1";

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest
                .newBuilder()
                .GET()
                .uri(URI.create(URL))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = null;
        try {
            response = httpClient.send(httpRequest,
                    HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        GeocodingResponse geocodingResponse = null;
        assert response != null;
        if (response.statusCode() == 200) {
            JSONObject jsonObject = new JSONObject(response.body());
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            ObjectMapper objectMapper = ObjectMapperCreator.getNewObjectMapper();
            try {
                geocodingResponse = objectMapper.readValue(jsonArray.get(0).toString(), GeocodingResponse.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //System.out.println(geocodingResponse); // dbg
        }
        return geocodingResponse;
    }
}
