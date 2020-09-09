package controllers;

import controllers_utils.CrudDialoger;
import controllers_utils.InputValidator;
import dao_interfaces.AccountDAO;
import factories.DAOFactory;
import factories.FormCheckerFactory;
import form_checker_interfaces.FormChecker;
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
    private FormCheckerFactory formCheckerFactory;
    private FormChecker formChecker;

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
            String email = getEmail();
            String password = getPassword();

            formCheckerFactory = FormCheckerFactory.getInstance();
            formChecker = formCheckerFactory.getFormChecker(this);

            if (formChecker.formHasSomeEmptyField(this)) {
                CrudDialoger.showAlertDialog(this, "Riempire tutto");
            } else {
                if (InputValidator.isValid(email)) {
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
            }
        });
    }

    public void loadSelectTypeScene() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/select_collection_type.fxml"));
        loginView.getRootPane().getChildren().setAll(pane);
    }

    public LoginView getLoginView() {
        return loginView;
    }

    public String getEmail() {
        return this.getLoginView().getEmail();
    }

    public String getPassword() {
        return this.getLoginView().getPassword();
    }
}