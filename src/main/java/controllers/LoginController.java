package controllers;

import controllers_utils.UserInputChecker;
import dao_interfaces.AccountDAO;
import factories.DAOFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Account;
import utils.ConfigFileReader;
import views.LoginView;

import java.io.IOException;
import java.util.Optional;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public class LoginController {
    private final LoginView loginView;

    public LoginController(LoginView loginView) {
        this.loginView = loginView;
    }

    public void setListenerOn(Button buttonLogin) {
        if (buttonLogin.getId().equals("buttonLogin")) {
            buttonLoginClicked();
        }
    }

    public void buttonLoginClicked() {
        loginView.getButtonLogin().setOnAction(event -> {
            String email, password;
            email = loginView.getTextFieldEmail().getText();
            password = loginView.getPasswordField().getText();

            if (!UserInputChecker.areEmpty(email, password)) {
                if (UserInputChecker.isValid(email)) {
                    Account account = new Account(email, password);
                    DAOFactory daoFactory = DAOFactory.getInstance();
                    AccountDAO accountDAO = daoFactory.getAccountDAO(ConfigFileReader.getProperty("account_storage_technology"));
                    try {
                        if (!accountDAO.login(account)) {
                            showAlertDialog("Qualcosa è andato storto durante il login");
                        } else {
                            loadSelectTypeScene();
                        }
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    showAlertDialog("Pattern email non valido");
                }
            } else {
                showAlertDialog("Riempi tutti i campi");
            }
        });
    }

    private void showAlertDialog(String alertMessage) {
        Stage stage = (Stage) loginView.getRootPane().getScene().getWindow();
        Alert.AlertType alertType = Alert.AlertType.ERROR;
        Alert alert = new Alert(alertType, "");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(stage);
        alert.setTitle("Attenzione");
        alert.getDialogPane().setHeaderText(alertMessage);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == ButtonType.OK) {
                alert.close();
            }
        }
    }

    public void loadSelectTypeScene() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/select_collection_type.fxml"));
        loginView.getRootPane().getChildren().setAll(pane);
    }
}
