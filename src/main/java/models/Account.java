package models;

import utils.AuthenticationResult;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public class Account implements Serializable {
    private String email;
    private char[] password;
    private final AuthenticationResult authenticationResult;

    public Account() {
        authenticationResult = AuthenticationResult.getInstance();
    }

    public Account(String email, char[] password) {
        this.email = email;
        this.password = password;
        authenticationResult = AuthenticationResult.getInstance();
    }

    public char[] getPassword() {
        return password;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    public void setPassword(String password) {
        this.password = password.toCharArray();
    }

    public String getIdToken() {
        return this.authenticationResult.getIdToken();
    }

    public void setIdToken(String idToken) {
        this.authenticationResult.setIdToken(idToken);
    }

    public void setTokenType(String tokenType) {
        this.authenticationResult.setTokenType(tokenType);
    }

    public void setAccessToken(String accessToken) {
        this.authenticationResult.setAccessToken(accessToken);
    }

    public void setTokenExpiresIn(int seconds) {
        this.authenticationResult.setExpiresIn(seconds);
    }

    public void setRefreshToken(String refreshToken) {
        this.authenticationResult.setRefreshToken(refreshToken);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public AuthenticationResult getAuthenticationResult() {
        return authenticationResult;
    }

    @Override
    public String toString() {
        return "Account{" +
                "email='" + email + '\'' +
                ", password=" + Arrays.toString(password) +
                ", authenticationResult=" + authenticationResult +
                '}';
    }
}
