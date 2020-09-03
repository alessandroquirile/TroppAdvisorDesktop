package controllers;

import dao_interfaces.RestaurantDAO;
import factories.DAOFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public class CrudRestaurantController implements Initializable {
    @FXML
    private AnchorPane rootPane;
    @FXML
    private javafx.scene.control.Button buttonElimina;
    @FXML
    private javafx.scene.control.Button buttonConferma;
    @FXML
    private javafx.scene.control.Button buttonAnnulla;
    @FXML
    private javafx.scene.control.Button buttonAiuto;
    @FXML
    private TableView<Object> tableView;
    @FXML
    private javafx.scene.control.Button buttonModifica;
    @FXML
    private javafx.scene.control.TextField textFieldNome;
    @FXML
    private javafx.scene.control.TextField textFieldDescrizione;
    @FXML
    private javafx.scene.control.TextField textFieldIndirizzo;
    @FXML
    private ChoiceBox<String> choiceBoxIndirizzo;
    @FXML
    private javafx.scene.control.TextField textFieldCittà;
    @FXML
    private ChoiceBox<Integer> choiceBoxRangePrezzo;
    @FXML
    private CheckBox checkBoxCertificatoDiEccellenza;
    @FXML
    private ChoiceBox<String> choiceBoxTipoDiCucina;
    @FXML
    private javafx.scene.control.Button buttonIndietro;
    @FXML
    private javafx.scene.control.Button buttonInserisci;
    @FXML
    private javafx.scene.control.TextField textFieldTipoDiCucina;
    @FXML
    private javafx.scene.control.TextField textFieldCAP;
    @FXML
    private TextField textFieldNumeroDiTelefono;
    private DAOFactory daoFactory;
    private RestaurantDAO restaurantDAO;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeChoiceBoxIndirizzo();
        initializeChoiceBoxTypeOfCuisine();
    }

    private void initializeChoiceBoxIndirizzo() {
        ObservableList<String> observableList = FXCollections.observableArrayList("Via", "Viale", "Vico", "Piazza", "Largo");
        choiceBoxIndirizzo.setItems(observableList);
        choiceBoxIndirizzo.getSelectionModel().selectFirst();
    }

    private void initializeChoiceBoxTypeOfCuisine() {
        ObservableList<String> listOfTypeOfCuisine = FXCollections.observableArrayList("Pizzeria", "Rosticceria", "Vegana");
        choiceBoxTipoDiCucina.setItems(listOfTypeOfCuisine);
        choiceBoxTipoDiCucina.getSelectionModel().selectFirst();
    }

    @FXML
    public void showSelectTypeStage(MouseEvent mouseEvent) {
        try {
            closeCurrentStage();
            Parent parent = FXMLLoader.load(CrudRestaurantController.class.getResource("/select_type.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeCurrentStage() {
        Stage thisStage = (Stage) rootPane.getScene().getWindow();
        thisStage.close();
    }

    @FXML
    public void buttonInserisciClicked(MouseEvent mouseEvent) {
        enableAllTextFields();
        enableAllChoiceBoxes();
        disableCRUDButtons();
        buttonConferma.setDisable(false);
        buttonAnnulla.setDisable(false);
        buttonIndietro.setDisable(true);
    }

    private void enableAllTextFields() {
        textFieldDescrizione.setDisable(false);
        textFieldIndirizzo.setDisable(false);
        textFieldCAP.setDisable(false);
        textFieldCittà.setDisable(false);
        textFieldTipoDiCucina.setDisable(false);
        textFieldNome.setDisable(false);
        textFieldNumeroDiTelefono.setDisable(false);
    }

    private void enableAllChoiceBoxes() {
        choiceBoxRangePrezzo.setDisable(false);
        checkBoxCertificatoDiEccellenza.setDisable(false);
        choiceBoxTipoDiCucina.setDisable(false);
        choiceBoxIndirizzo.setDisable(false);
    }

    private void disableCRUDButtons() {
        buttonInserisci.setDisable(true);
        buttonModifica.setDisable(true);
        buttonElimina.setDisable(true);
    }

    @FXML
    public void buttonAnnullaClicked(MouseEvent mouseEvent) {
        showConfirmationDialogPopup();
    }

    private void showConfirmationDialogPopup() {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        Alert.AlertType alertType = Alert.AlertType.CONFIRMATION;
        Alert alert = new Alert(alertType, "");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(stage);
        alert.setTitle("Attenzione");
        alert.getDialogPane().setHeaderText("Sei sicuro di voler annullare l'operazione?");
        alert.getDialogPane().setContentText("Cliccando su OK annullerai l'operazione corrente");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == ButtonType.OK) {
                setViewsAsDefault();
            }
        }
    }

    private void setViewsAsDefault() {
        buttonIndietro.setDisable(false);
        buttonInserisci.setDisable(false);
        buttonModifica.setDisable(true);
        buttonElimina.setDisable(true);
        buttonConferma.setDisable(true);
        buttonAnnulla.setDisable(true);
        buttonAiuto.setDisable(false);
        textFieldNome.setDisable(true);
        textFieldDescrizione.setDisable(true);
        textFieldIndirizzo.setDisable(true);
        choiceBoxIndirizzo.setDisable(true);
        textFieldCAP.setDisable(true);
        textFieldCittà.setDisable(true);
        choiceBoxRangePrezzo.setDisable(true);
        checkBoxCertificatoDiEccellenza.setDisable(true);
        choiceBoxTipoDiCucina.setDisable(true);
        textFieldTipoDiCucina.setDisable(true);
        textFieldNome.setText("");
        textFieldDescrizione.setText("");
        textFieldIndirizzo.setText("");
        textFieldCAP.setText("");
        textFieldCittà.setText("");
        textFieldTipoDiCucina.setText("");
        textFieldNumeroDiTelefono.setText("");
        initializeChoiceBoxIndirizzo();
        initializeChoiceBoxTypeOfCuisine();
    }

    @FXML
    public void buttonConfermaClicked(MouseEvent mouseEvent) {
        System.out.println("Cliccato conferma"); //dbg
        /*Restaurant restaurant = new Restaurant();
        // setter su restaurant coi dati presi dal form
        daoFactory = DAOFactory.getInstance();
        restaurantDAO = daoFactory.getRestaurantDAO(ConfigFileReader.getProperty("restaurant_storage_technology"));
        restaurantDAO.add(restaurant);*/
    }

    @FXML
    public void buttonAiutoClicked(MouseEvent mouseEvent) {
        showInformationHelperDialogPopup();
    }

    private void showInformationHelperDialogPopup() {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        Alert.AlertType alertType = Alert.AlertType.INFORMATION;
        Alert alert = new Alert(alertType, "");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(stage);
        alert.setTitle("Come usare l'interfaccia?");
        alert.getDialogPane().setHeaderText("Seleziona un'operazione CRUD in alto a sinistra " +
                "e poi conferma la tua scelta dopo aver inserito i dati nel form");
        alert.getDialogPane().setContentText("Per aggiornare o eliminare un record già esistente, selezionarlo dapprima dalla tabella");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == ButtonType.OK) {
                setViewsAsDefault();
            }
        }
    }
}