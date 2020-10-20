package geocoder_implementations;

import com.fasterxml.jackson.databind.ObjectMapper;
import controllers_utils.ObjectMapperCreator;
import geocoder_interfaces.Geocoder;
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
public class Geocoder_APIPositionStack implements Geocoder {
    private static final StringBuffer API_KEY = new StringBuffer("3986e2581d9bf3b4767b73a37e478373");

    public Geocoder_APIPositionStack() {

    }

    @Override
    public GeocodingResponse forward(String address) {
        String URL = "http://api.positionstack.com/v1/forward?";
        URL += "access_key=" + API_KEY + "&query=" + URLEncoder.encode(address, StandardCharsets.UTF_8) + "&limit=1";

        // API Position Stack free tier pone dei limiti sul numero di richieste per secondo e alle volte se ci sono tante
        // richieste, anziché restituire un file json ben formato, restituisce un array vuoto.
        // È necessario quindi controllare che l'array non sia vuoto e restituire quell'oggetto ricavato; altrimenti rifare
        do {
            HttpClient httpClient = HttpClient
                    .newBuilder()
                    .version(HttpClient.Version.HTTP_1_1)
                    .build();

            HttpRequest httpRequest = HttpRequest
                    .newBuilder()
                    .GET()
                    .uri(URI.create(URL))
                    .header("Content-Type", "application/json")
                    .build();

            HttpResponse<String> response;
            try {
                response = httpClient.send(httpRequest,
                        HttpResponse.BodyHandlers.ofString());
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }

            if (response != null && response.statusCode() == 200) {
                JSONObject jsonObject = new JSONObject(response.body());
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                ObjectMapper objectMapper = ObjectMapperCreator.getNewObjectMapper();
                try {
                    if (jsonArray.length() > 0)
                        return objectMapper.readValue(jsonArray.get(0).toString(), GeocodingResponse.class);
                } catch (IOException ignored) {
                }
            }
        } while (true);

    }
}
