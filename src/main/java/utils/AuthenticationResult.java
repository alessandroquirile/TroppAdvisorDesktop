package utils;

import java.time.Instant;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public class AuthenticationResult {
    private static AuthenticationResult singletonInstance = null;
    private String idToken;
    private String tokenType;
    private String accessToken;
    private String refreshToken;
    private int expiresIn;

    private AuthenticationResult() {

    }

    public static synchronized AuthenticationResult getInstance() {
        if (singletonInstance == null)
            singletonInstance = new AuthenticationResult();
        return singletonInstance;
    }

    // Non è utilizzato ma potrebbe tornare utile
    public boolean isExpired(Instant expiration) {
        return !Instant.now().isBefore(expiration);
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    @Override
    public String toString() {
        return "AuthenticationResult{" +
                "idToken='" + idToken + '\'' +
                ", tokenType='" + tokenType + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", expiresIn=" + expiresIn +
                '}';
    }
}
