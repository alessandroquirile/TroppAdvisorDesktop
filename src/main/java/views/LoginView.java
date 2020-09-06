package views;

import controllers.LoginController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public class LoginView implements Initializable {

    @FXML
    private TextField textFieldEmail;
    @FXML
    private Button buttonLogin;
    @FXML
    private PasswordField passwordField;
    @FXML
    private AnchorPane rootPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LoginController loginController = new LoginController(this);
        loginController.setListenerOn(buttonLogin);
    }

    public TextField getTextFieldEmail() {
        return textFieldEmail;
    }

    public Button getButtonLogin() {
        return buttonLogin;
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    public AnchorPane getRootPane() {
        return rootPane;
    }
}