package controllers;

import controllers_utils.CrudDialoger;
import controllers_utils.UserInputChecker;
import dao_interfaces.AccountDAO;
import factories.DAOFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.Account;
import utils.ConfigFileReader;
import views.LoginView;

import java.io.IOException;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public class LoginController extends Controller {
    private final LoginView loginView;

    public LoginController(LoginView loginView) {
        this.loginView = loginView;
    }

    @Override
    public Stage getStage() {
        return (Stage) this.loginView.getRootPane().getScene().getWindow().getScene().getWindow();
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
                            CrudDialoger.showAlertDialog(this, "Qualcosa è andato storto durante il login");
                        } else {
                            loadSelectTypeScene();
                        }
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    CrudDialoger.showAlertDialog(this, "Pattern email non valido");
                }
            } else {
                CrudDialoger.showAlertDialog(this, "Riempire tutti i campi");
            }
        });
    }

    public void loadSelectTypeScene() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/select_collection_type.fxml"));
        loginView.getRootPane().getChildren().setAll(pane);
    }
}
