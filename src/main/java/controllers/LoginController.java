package controllers;

import dao_interfaces.AccountDAO;
import factories.DAOFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import models.Account;
import utils.ConfigFileReader;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public class LoginController {

    @FXML
    private TextField textFieldEmail;
    @FXML
    private Button buttonLogin;
    @FXML
    private PasswordField passwordField;
    @FXML
    private AnchorPane rootPane;
    private DAOFactory daoFactory;
    private AccountDAO accountDAO;

    @FXML
    public void doLogin() throws IOException {
        String email, password;
        email = textFieldEmail.getText();
        password = passwordField.getText();

        if (!areEmpty(email, password)) {
            if (isValid(email)) {
                Account account = new Account();
                account.setEmail(email);
                account.setPassword(password);
                daoFactory = DAOFactory.getInstance();
                accountDAO = daoFactory.getAccountDAO(ConfigFileReader.getProperty("account_storage_technology"));
                if (!accountDAO.login(account)) {
                    System.out.println("Qualcosa è andato storto durante il login");
                } else {
                    showSelectTypeScene();
                }
            } else {
                System.out.println("Pattern email non valido");
            }
        } else {
            System.out.println("Riempire tutti i campi");
        }
    }

    public void showSelectTypeScene() throws IOException {
        AnchorPane pane = FXMLLoader.load(LoginController.class.getResource("/select_type.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    public static boolean isValid(String email) {
        String emailRegExp = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern emailPattern = Pattern.compile(emailRegExp, Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailPattern.matcher(email);
        return matcher.find();
    }

    public static boolean areEmpty(String... strings) {
        for (String string : strings)
            if (string.equals(""))
                return true;
        return false;
    }
}