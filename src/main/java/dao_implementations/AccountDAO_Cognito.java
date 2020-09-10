package dao_implementations;

import dao_interfaces.AccountDAO;
import models.Account;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public class AccountDAO_Cognito implements AccountDAO {

    @Override
    public boolean login(Account account) throws IOException, InterruptedException {
        final String URL = getUrlLoginFor(account);
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest
                .newBuilder()
                .GET()
                .header("accept", "application/json")
                .header("Content-Type", "application/json")
                .uri(URI.create(URL))
                .build();
        HttpResponse<String> httpResponses = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        //System.out.println("Header:\n" +httpResponses.headers() + "\nBody:\n" + httpResponses.body()); // dbg
        return httpResponses.statusCode() == 200;
    }

    private String getUrlLoginFor(Account account) {
        String URL = "http://Troppadvisorserver-env.eba-pfsmp3kx.us-east-1.elasticbeanstalk.com/cognito/admin-login?";
        URL = URL.concat("username=" + account.getEmail());
        URL = URL.concat("&password=" + account.getPassword());
        return URL;
    }
}