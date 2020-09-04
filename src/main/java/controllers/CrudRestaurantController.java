package controllers;

import controllers_utils.TableSettersGetters;
import dao_interfaces.RestaurantDAO;
import factories.DAOFactory;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Address;
import models.Restaurant;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private ChoiceBox<String> choiceBoxIndirizzo;
    @FXML
    private javafx.scene.control.TextField textFieldCittà;
    @FXML
    private CheckBox checkBoxCertificatoDiEccellenza;
    @FXML
    private javafx.scene.control.Button buttonIndietro;
    @FXML
    private javafx.scene.control.Button buttonInserisci;
    @FXML
    private javafx.scene.control.TextField textFieldCAP;
    @FXML
    private TextField textFieldNumeroDiTelefono;
    @FXML
    private Button buttonCaricaFoto;
    @FXML
    private ListView<String> listViewFotoPath;
    @FXML
    private TableColumn<TableSettersGetters, String> typeOfCuisineNameColumn;
    @FXML
    private TableColumn<TableSettersGetters, CheckBox> typeOfCuisineSelectColumn;
    @FXML
    private TableView<TableSettersGetters> tableViewTypeOfCuisine;
    @FXML
    private TextField textFieldPrezzoMedio;
    @FXML
    private TextField txtFieldNumeroCivico;
    @FXML
    private TextField textFieldProvincia;
    @FXML
    private TextField textFieldStrada;
    private DAOFactory daoFactory;
    private RestaurantDAO restaurantDAO;

    public static boolean isValid(String telephoneNumber) {
        String telephoneNumberRegExp = "^([0-9]*\\-?\\ ?\\/?[0-9]*)$";
        if (telephoneNumber.length() == 10) {
            Pattern telephoneNumberPattern = Pattern.compile(telephoneNumberRegExp, Pattern.CASE_INSENSITIVE);
            Matcher matcher = telephoneNumberPattern.matcher(telephoneNumber);
            return matcher.find();
        } else {
            return false;
        }
    }

    public static boolean isValidNumberGreaterOrEqualToZero(String number) {
        String numberRegExp = "^[0-9]+$";
        Pattern numberGreaterOrEqualToZeroPattern = Pattern.compile(numberRegExp, Pattern.CASE_INSENSITIVE);
        Matcher matcher = numberGreaterOrEqualToZeroPattern.matcher(number);
        return matcher.find();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setViewsAsDefault();
    }

    private void initializeChoiceBoxIndirizzo() {
        ObservableList<String> observableList = FXCollections.observableArrayList("Via", "Viale", "Vico", "Piazza", "Largo");
        choiceBoxIndirizzo.setItems(observableList);
        choiceBoxIndirizzo.getSelectionModel().selectFirst();
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

    private void initializeTableViewTypeOfCuisine() {
        tableViewTypeOfCuisine.setDisable(true);
        ObservableList<TableSettersGetters> list = FXCollections.observableArrayList();
        list.add(new TableSettersGetters("Mediterranea", new CheckBox()));
        list.add(new TableSettersGetters("Pizzeria", new CheckBox()));
        list.add(new TableSettersGetters("Ristorante", new CheckBox()));
        list.add(new TableSettersGetters("Rosticceria", new CheckBox()));
        list.add(new TableSettersGetters("Vegana", new CheckBox()));
        tableViewTypeOfCuisine.setItems(list);
        typeOfCuisineNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        typeOfCuisineSelectColumn.setCellValueFactory(new PropertyValueFactory<>("checkBox"));
    }

    private void setViewsAsDefault() {
        buttonIndietro.setDisable(false);
        buttonInserisci.setDisable(false);
        buttonModifica.setDisable(true);
        textFieldNumeroDiTelefono.setDisable(true);
        textFieldStrada.setDisable(true);
        txtFieldNumeroCivico.setDisable(true);
        textFieldProvincia.setDisable(true);
        buttonElimina.setDisable(true);
        buttonConferma.setDisable(true);
        buttonAnnulla.setDisable(true);
        buttonAiuto.setDisable(false);
        textFieldNome.setDisable(true);
        textFieldPrezzoMedio.setDisable(true);
        choiceBoxIndirizzo.setDisable(true);
        textFieldCAP.setDisable(true);
        textFieldCittà.setDisable(true);
        checkBoxCertificatoDiEccellenza.setDisable(true);
        tableViewTypeOfCuisine.setDisable(true);
        textFieldNome.setText("");
        textFieldCAP.setText("");
        textFieldCittà.setText("");
        textFieldPrezzoMedio.setText("");
        textFieldNumeroDiTelefono.setText("");
        textFieldStrada.setText("");
        txtFieldNumeroCivico.setText("");
        textFieldProvincia.setText("");
        initializeChoiceBoxIndirizzo();
        initializeTableViewTypeOfCuisine();
        buttonCaricaFoto.setDisable(true);
        listViewFotoPath.setDisable(true);
        listViewFotoPath.getItems().clear();
    }

    private void enableAllChoiceBoxes() {
        checkBoxCertificatoDiEccellenza.setDisable(false);
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

    @FXML
    public void buttonInserisciClicked(MouseEvent mouseEvent) {
        enableAllTextFields();
        enableAllChoiceBoxes();
        disableCRUDButtons();
        buttonConferma.setDisable(false);
        buttonAnnulla.setDisable(false);
        buttonIndietro.setDisable(true);
        tableViewTypeOfCuisine.setDisable(false);
        buttonCaricaFoto.setDisable(false);
        textFieldNumeroDiTelefono.setDisable(false);
        listViewFotoPath.setDisable(false);
    }

    private boolean formHasSomeEmptyFields() {
        return textFieldNome.getText().isEmpty() || textFieldStrada.getText().isEmpty() ||
                txtFieldNumeroCivico.getText().isEmpty() || textFieldProvincia.getText().isEmpty() ||
                textFieldProvincia.getText().isEmpty() || textFieldCAP.getText().isEmpty() ||
                textFieldCittà.getText().isEmpty() || textFieldPrezzoMedio.getText().isEmpty() ||
                textFieldNumeroDiTelefono.getText().isEmpty() || Bindings.isEmpty(listViewFotoPath.getItems()).get() ||
                !hasAtLeastOneTypeOfCuisineSelected();
    }

    @FXML
    public void buttonConfermaClicked(MouseEvent mouseEvent) {
        // nome, strada, civico, provincia, cap, città, prezzo medio, numero di telefono, foto, tipo di cucina
        if (formHasSomeEmptyFields()) {
            System.out.println("Riempire tutti i campi");
        } else {
            if (isValid(textFieldNumeroDiTelefono.getText())) {
                if (isValidNumberGreaterOrEqualToZero(textFieldPrezzoMedio.getText())) {
                    if (isValidNumberGreaterOrEqualToZero(txtFieldNumeroCivico.getText())) {
                        Restaurant restaurant = createRestaurantWithFormData();
                        System.out.println(restaurant.toString()); // dbg
                        /*daoFactory = DAOFactory.getInstance();
                        restaurantDAO = daoFactory.getRestaurantDAO(ConfigFileReader.getProperty("restaurant_storage_technology"));
                        restaurantDAO.add(restaurant);*/
                    } else {
                        System.out.println("Civico non valido");
                    }
                } else {
                    System.out.println("Prezzo medio non valido");
                }
            } else {
                System.out.println("Telefono non valido");
            }

        }
    }


    private Restaurant createRestaurantWithFormData() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(textFieldNome.getText());
        restaurant.setAddress(new Address(
                choiceBoxIndirizzo.getValue(),
                textFieldStrada.getText(),
                txtFieldNumeroCivico.getText(),
                textFieldCittà.getText(),
                textFieldProvincia.getText(),
                textFieldCAP.getText()
        ));
        restaurant.setAvaragePrice(Integer.parseInt(textFieldPrezzoMedio.getText()));
        restaurant.setPhoneNumber(textFieldNumeroDiTelefono.getText());
        restaurant.setHasCertificateOfExcellence(checkBoxCertificatoDiEccellenza.isSelected());
        restaurant.setImages(listViewFotoPath.getItems());
        List<String> cuisineSelected = new ArrayList<>();
        for (TableSettersGetters tableSettersGetters : tableViewTypeOfCuisine.getItems()) {
            if (tableSettersGetters.getCheckBox().isSelected())
                cuisineSelected.add(tableSettersGetters.getName());
        }
        restaurant.setTypeOfCuisine(cuisineSelected);
        return restaurant;
    }

    private boolean hasAtLeastOneTypeOfCuisineSelected() {
        for (TableSettersGetters tableSettersGetters : tableViewTypeOfCuisine.getItems()) {
            if (tableSettersGetters.getCheckBox().isSelected()) {
                return true;
            }
        }
        return false;
    }

    private void printSelectedTypeOfCuisine() {
        for (TableSettersGetters tableSettersGetters : tableViewTypeOfCuisine.getItems())
            if (tableSettersGetters.getCheckBox().isSelected())
                System.out.println(tableSettersGetters.getName() + " selected");
    }

    private void enableAllTextFields() {
        textFieldCAP.setDisable(false);
        textFieldCittà.setDisable(false);
        textFieldNome.setDisable(false);
        textFieldNumeroDiTelefono.setDisable(false);
        textFieldPrezzoMedio.setDisable(false);
        textFieldStrada.setDisable(false);
        txtFieldNumeroCivico.setDisable(false);
        textFieldProvincia.setDisable(false);
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

    @FXML
    public void buttonCaricaFotoClicked(MouseEvent mouseEvent) {
        multiFileSelection();
    }

    public void multiFileSelection() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        FileChooser.ExtensionFilter imageFilter =
                new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.jpeg");
        fileChooser.getExtensionFilters().add(imageFilter);
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(null);
        if (selectedFiles != null) {
            for (File selectedFile : selectedFiles) {
                listViewFotoPath.getItems().add(selectedFile.getAbsolutePath());
            }
        } else {
            System.out.println("Selected files are not valid");
        }
    }
}