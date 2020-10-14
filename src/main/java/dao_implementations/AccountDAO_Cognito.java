package dao_implementations;

import com.fasterxml.jackson.databind.ObjectMapper;
import controllers_utils.ObjectMapperCreator;
import dao_interfaces.AccountDAO;
import models.Account;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public class AccountDAO_Cognito extends RestDAO implements AccountDAO {
    private static final String REPOSITORY = "cognito";

    /**
     * Consente ad un amministratore di effettuare un login. L'account è caratterizzato da una mail e una password.
     * La password deve avere lunghezza >= 8, almeno una maiuscola, almeno una minuscola, almeno un carattere speciale e un numero.
     * La password è case sensitive
     *
     * @param account l'account dell'amministratore (non può essere null)
     * @return true se il login è stato compiuto; false altrimenti
     */
    @Override
    public boolean login(@NotNull Account account) throws IOException, InterruptedException {

        // La scelta di un controllo sembrerebbe inutile, ma permette al sistema un miglioramento in termini di performance
        // dal momento che non sarà più inviato qualsiasi tipo di dato al server, ma in primis controllato in locale
        // senza quindi appesantire il carico del server

        if (account.getEmail() == null)
            return false;

        if (account.getPassword() == null)
            return false;

        if (!respectPattern(account.getPassword()))
            return false;

        String URL = getBaseUrl();
        URL = URL.concat("/" + REPOSITORY);
        URL = URL.concat("/admin-login");

        final Map<String, Object> values = new HashMap<>();
        values.put("key", account.getEmail());
        values.put("password", account.getPassword());

        ObjectMapper objectMapper = ObjectMapperCreator.getNewObjectMapper();
        String requestBody = objectMapper.writeValueAsString(values);

        HttpClient httpClient = getHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create(URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());

        return response.statusCode() == 200 && setAuthenticationResultIntoAccount(response, account);
    }

    private boolean respectPattern(char[] password) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(password);
        Pattern passwordPattern = Pattern.compile(
                "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[.@$!%*?&])[A-Za-z\\d@$!%*?&.]{8,}$",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = passwordPattern.matcher(stringBuffer);
        return matcher.find();
    }

    private boolean setAuthenticationResultIntoAccount(HttpResponse<String> response, Account account) {
        JSONObject jsonObject = new JSONObject(response.body());
        JSONObject authenticationResult = (JSONObject) jsonObject.get("authenticationResult");
        account.setIdToken(authenticationResult.getString("idToken"));
        account.setTokenType(authenticationResult.getString("tokenType"));
        account.setAccessToken(authenticationResult.getString("accessToken"));
        account.setTokenExpiresIn(authenticationResult.getInt("expiresIn"));
        account.setRefreshToken(authenticationResult.getString("refreshToken"));
        return true;
    }
}