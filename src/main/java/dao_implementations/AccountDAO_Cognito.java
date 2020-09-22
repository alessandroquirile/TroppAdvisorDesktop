package dao_implementations;

import com.fasterxml.jackson.databind.ObjectMapper;
import controllers_utils.ObjectMapperCreator;
import dao_interfaces.AccountDAO;
import models.Account;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public class AccountDAO_Cognito implements AccountDAO {

    @Override
    public boolean login(Account account) throws IOException, InterruptedException {
        final String URL = getUrlLoginFor(account);

        final Map<String, Object> values = new HashMap<>();
        values.put("key", account.getEmail());
        values.put("password", account.getPassword());

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

        //System.out.println("Header:\n" +response.headers() + "\nBody:\n" + response.body()); // dbg

        JSONObject jsonObject = new JSONObject(response.body());
        JSONObject authenticationResult = (JSONObject) jsonObject.get("authenticationResult");

        account.setIdToken(authenticationResult.getString("idToken"));
        account.setTokenType(authenticationResult.getString("tokenType"));
        account.setAccessToken(authenticationResult.getString("accessToken"));
        account.setTokenExpiresIn(authenticationResult.getInt("expiresIn"));
        account.setRefreshToken(authenticationResult.getString("refreshToken"));

        System.out.println(account.getAuthenticationResult().toString()); // dbg

        return response.statusCode() == 200;
    }

    private String getUrlLoginFor(Account account) {
        String URL = "http://Troppadvisorserver-env.eba-pfsmp3kx.us-east-1.elasticbeanstalk.com/cognito/admin-login?";
        URL = URL.concat("username=" + account.getEmail());
        URL = URL.concat("&password=" + account.getPassword());
        return URL;
    }
}