package controllers;

import controllers_utils.TableSettersGetters;
import controllers_utils.UserInputChecker;
import dao_interfaces.RestaurantDAO;
import factories.DAOFactory;
import geocoding.Geocoder;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Address;
import models.Restaurant;
import models_helpers.Point;
import utils.ConfigFileReader;
import views.CrudRestaurantView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public class CrudRestaurantController {

    private final CrudRestaurantView crudRestaurantView;
    private DAOFactory daoFactory;
    private RestaurantDAO restaurantDAO;

    public CrudRestaurantController(CrudRestaurantView crudRestaurantView) {
        this.crudRestaurantView = crudRestaurantView;
    }

    public void setListenerOn(Button button) {
        switch (button.getId()) {
            case "buttonInserisci":
                buttonInserisciClicked();
                break;
            case "buttonAnnulla":
                buttonAnnullaClicked();
                break;
            case "buttonIndietro":
                buttonIndietroClicked();
                break;
            case "buttonConferma":
                buttonConfermaClicked();
                break;
            case "buttonAiuto":
                buttonAiutoClicked();
                break;
            case "buttonCaricaFoto":
                buttonCaricaClicked();
                break;
        }
    }

    private void buttonInserisciClicked() {
        crudRestaurantView.getButtonInserisci().setOnAction(event -> {
            enableAllTextFields();
            enableAllChoiceBoxes();
            disableCRUDButtons();
            crudRestaurantView.getButtonConferma().setDisable(false);
            crudRestaurantView.getButtonAnnulla().setDisable(false);
            crudRestaurantView.getButtonIndietro().setDisable(true);
            crudRestaurantView.getTableViewTypeOfCuisine().setDisable(false);
            crudRestaurantView.getButtonCaricaFoto().setDisable(false);
            crudRestaurantView.getTextFieldNumeroDiTelefono().setDisable(false);
            crudRestaurantView.getListViewFotoPath().setDisable(false);
            crudRestaurantView.getComboBoxOrarioAperturaMattutina().setDisable(false);
            crudRestaurantView.getComboBoxOrarioChiusuraMattutina().setDisable(false);
            crudRestaurantView.getComboBoxOrarioAperturaSerale().setDisable(false);
            crudRestaurantView.getComboBoxOrarioChiusuraSerale().setDisable(false);
        });
    }

    private void buttonConfermaClicked() {
        crudRestaurantView.getButtonConferma().setOnAction(event -> {
            // nome, strada, civico, provincia, cap, città, prezzo medio, numero di telefono, foto, tipo di cucina, orario
            if (formHasSomeEmptyFields()) {
                showAlertDialog("Riempire tutti i campi");
            } else {
                if (UserInputChecker.isValidTelephoneNumber(crudRestaurantView.getTextFieldNumeroDiTelefono().getText())) {
                    if (UserInputChecker.isValidNumberGreaterOrEqualToZero(crudRestaurantView.getTextFieldPrezzoMedio().getText())) {
                        if (UserInputChecker.isValidNumberGreaterOrEqualToZero(crudRestaurantView.getTxtFieldNumeroCivico().getText())) {
                            // Controllo sull'orario di chiusura post quello di apertura?
                            if (UserInputChecker.isValidOpeningTimeAtMorning(crudRestaurantView.getComboBoxOrarioAperturaMattutina().getValue(),
                                    crudRestaurantView.getComboBoxOrarioChiusuraMattutina().getValue())) {
                                Restaurant restaurant = createRestaurantWithFormData();
                                // System.out.println("DBG: " + restaurant.toString()); // dbg
                                daoFactory = DAOFactory.getInstance();
                                restaurantDAO = daoFactory.getRestaurantDAO(ConfigFileReader.getProperty("restaurant_storage_technology"));
                                try {
                                    if (!restaurantDAO.add(restaurant)) {
                                        showAlertDialog("Qualcosa è andato storto durante l'inserimento");
                                    } else {
                                        System.out.println("Ristorante inserito correttamente");
                                    }
                                } catch (IOException | InterruptedException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                showAlertDialog("Orario mattutino non valido");
                            }
                        } else {
                            showAlertDialog("Numero civico non valido");
                        }
                    } else {
                        showAlertDialog("Prezzo medio non valido. Inserire un intero");
                    }
                } else {
                    showAlertDialog("Numero di telefono non valido");
                }
            }
        });
    }

    private void buttonAiutoClicked() {
        crudRestaurantView.getButtonAiuto().setOnAction(event -> showHelpDialog());
    }

    private void showHelpDialog() {
        Stage stage = (Stage) crudRestaurantView.getRootPane().getScene().getWindow();
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
                alert.close();
            }
        }
    }

    private void showAlertDialog(String alertMessage) {
        Stage stage = (Stage) crudRestaurantView.getRootPane().getScene().getWindow();
        Alert.AlertType alertType = Alert.AlertType.INFORMATION;
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

    private void buttonCaricaClicked() {
        crudRestaurantView.getButtonCaricaFoto().setOnAction(event -> multiFileSelection());
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
                crudRestaurantView.getListViewFotoPath().getItems().add(selectedFile.getAbsolutePath());
            }
        } else {
            showAlertDialog("I file selezionati non sono validi");
        }
    }

    private Restaurant createRestaurantWithFormData() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(crudRestaurantView.getTextFieldNome().getText());
        restaurant.setAddress(createAddressWithFormData());
        restaurant.setAvaragePrice(Integer.parseInt(crudRestaurantView.getTextFieldPrezzoMedio().getText()));
        restaurant.setPhoneNumber(crudRestaurantView.getTextFieldNumeroDiTelefono().getText());
        restaurant.setHasCertificateOfExcellence(crudRestaurantView.getCheckBoxCertificatoDiEccellenza().isSelected());
        restaurant.setImages(crudRestaurantView.getListViewFotoPath().getItems());
        restaurant.setTypeOfCuisine(createTypeOfCuisineWithFormData());
        restaurant.setPoint(new Point(Geocoder.reverseGeocoding(createEligibleAddressForGeocoding()).getLat(),
                Geocoder.reverseGeocoding(createEligibleAddressForGeocoding()).getLng()));
        restaurant.setAddedDate(getCurrentDate());
        restaurant.setOpeningTime(getOpeningTimeWithFormData());
        return restaurant;
    }

    private Address createAddressWithFormData() {
        return new Address(
                crudRestaurantView.getChoiceBoxIndirizzo().getValue(),
                crudRestaurantView.getTextFieldStrada().getText(),
                crudRestaurantView.getTxtFieldNumeroCivico().getText(),
                crudRestaurantView.getTextFieldCittà().getText(),
                crudRestaurantView.getTextFieldProvincia().getText(),
                crudRestaurantView.getTextFieldCAP().getText());
    }

    private List<String> createTypeOfCuisineWithFormData() {
        List<String> cuisineSelected = new ArrayList<>();
        for (TableSettersGetters tableSettersGetters : crudRestaurantView.getTableViewTypeOfCuisine().getItems()) {
            if (tableSettersGetters.getCheckBox().isSelected())
                cuisineSelected.add(tableSettersGetters.getName());
        }
        return cuisineSelected;
    }

    private String createEligibleAddressForGeocoding() {
        return crudRestaurantView.getChoiceBoxIndirizzo().getValue() + " " + crudRestaurantView.getTextFieldStrada().getText() + ", " +
                crudRestaurantView.getTxtFieldNumeroCivico().getText() + ", " + crudRestaurantView.getTextFieldCittà().getText() + ", " + crudRestaurantView.getTextFieldCAP().getText() +
                ", " + crudRestaurantView.getTextFieldProvincia().getText();
    }

    private String getCurrentDate() {
        return new Date().toString();
    }

    private String getOpeningTimeWithFormData() {
        return crudRestaurantView.getComboBoxOrarioAperturaMattutina().getValue() + " - " + crudRestaurantView.getComboBoxOrarioChiusuraMattutina().getValue() +
                " " + crudRestaurantView.getComboBoxOrarioAperturaSerale().getValue() + " - " + crudRestaurantView.getComboBoxOrarioChiusuraSerale().getValue();
    }

    private boolean formHasSomeEmptyFields() {
        return crudRestaurantView.getTextFieldNome().getText().isEmpty() || crudRestaurantView.getTextFieldStrada().getText().isEmpty() ||
                crudRestaurantView.getTxtFieldNumeroCivico().getText().isEmpty() || crudRestaurantView.getTextFieldProvincia().getText().isEmpty() ||
                crudRestaurantView.getTextFieldCAP().getText().isEmpty() ||
                crudRestaurantView.getTextFieldCittà().getText().isEmpty() || crudRestaurantView.getTextFieldPrezzoMedio().getText().isEmpty() ||
                crudRestaurantView.getTextFieldNumeroDiTelefono().getText().isEmpty() || Bindings.isEmpty(crudRestaurantView.getListViewFotoPath().getItems()).get() ||
                !hasAtLeastOneTypeOfCuisineSelected();
    }

    private boolean hasAtLeastOneTypeOfCuisineSelected() {
        for (TableSettersGetters tableSettersGetters : crudRestaurantView.getTableViewTypeOfCuisine().getItems()) {
            if (tableSettersGetters.getCheckBox().isSelected()) {
                return true;
            }
        }
        return false;
    }

    private void enableAllTextFields() {
        crudRestaurantView.getTextFieldCAP().setDisable(false);
        crudRestaurantView.getTextFieldCittà().setDisable(false);
        crudRestaurantView.getTextFieldNome().setDisable(false);
        crudRestaurantView.getTextFieldNumeroDiTelefono().setDisable(false);
        crudRestaurantView.getTextFieldPrezzoMedio().setDisable(false);
        crudRestaurantView.getTextFieldStrada().setDisable(false);
        crudRestaurantView.getTxtFieldNumeroCivico().setDisable(false);
        crudRestaurantView.getTextFieldProvincia().setDisable(false);
    }

    private void enableAllChoiceBoxes() {
        crudRestaurantView.getCheckBoxCertificatoDiEccellenza().setDisable(false);
        crudRestaurantView.getChoiceBoxIndirizzo().setDisable(false);
    }

    private void disableCRUDButtons() {
        crudRestaurantView.getButtonInserisci().setDisable(true);
        crudRestaurantView.getButtonModifica().setDisable(true);
        crudRestaurantView.getButtonElimina().setDisable(true);
    }

    private void buttonAnnullaClicked() {
        crudRestaurantView.getButtonAnnulla().setOnAction(event -> setViewsAsDefault());
    }

    private void buttonIndietroClicked() {
        crudRestaurantView.getButtonIndietro().setOnAction(event -> showSelectTypeStage());
    }

    public void setViewsAsDefault() {
        crudRestaurantView.getButtonIndietro().setDisable(false);
        crudRestaurantView.getButtonInserisci().setDisable(false);
        crudRestaurantView.getButtonModifica().setDisable(true);
        crudRestaurantView.getTextFieldNumeroDiTelefono().setDisable(true);
        crudRestaurantView.getTextFieldStrada().setDisable(true);
        crudRestaurantView.getTxtFieldNumeroCivico().setDisable(true);
        crudRestaurantView.getTextFieldProvincia().setDisable(true);
        crudRestaurantView.getButtonElimina().setDisable(true);
        crudRestaurantView.getButtonConferma().setDisable(true);
        crudRestaurantView.getButtonAnnulla().setDisable(true);
        crudRestaurantView.getButtonAiuto().setDisable(false);
        crudRestaurantView.getTextFieldNome().setDisable(true);
        crudRestaurantView.getTextFieldPrezzoMedio().setDisable(true);
        crudRestaurantView.getChoiceBoxIndirizzo().setDisable(true);
        crudRestaurantView.getTextFieldCAP().setDisable(true);
        crudRestaurantView.getTextFieldCittà().setDisable(true);
        crudRestaurantView.getCheckBoxCertificatoDiEccellenza().setDisable(true);
        crudRestaurantView.getTableViewTypeOfCuisine().setDisable(true);
        crudRestaurantView.getTextFieldNome().setText("");
        crudRestaurantView.getTextFieldCAP().setText("");
        crudRestaurantView.getTextFieldCittà().setText("");
        crudRestaurantView.getTextFieldPrezzoMedio().setText("");
        crudRestaurantView.getTextFieldNumeroDiTelefono().setText("");
        crudRestaurantView.getTextFieldStrada().setText("");
        crudRestaurantView.getTxtFieldNumeroCivico().setText("");
        crudRestaurantView.getTextFieldProvincia().setText("");
        crudRestaurantView.getComboBoxOrarioAperturaMattutina().setDisable(true);
        crudRestaurantView.getComboBoxOrarioChiusuraMattutina().setDisable(true);
        crudRestaurantView.getComboBoxOrarioAperturaSerale().setDisable(true);
        crudRestaurantView.getComboBoxOrarioChiusuraSerale().setDisable(true);
        crudRestaurantView.getComboBoxOrarioAperturaMattutina().getSelectionModel().selectFirst();
        crudRestaurantView.getComboBoxOrarioChiusuraMattutina().getSelectionModel().selectFirst();
        crudRestaurantView.getComboBoxOrarioAperturaSerale().getSelectionModel().selectFirst();
        crudRestaurantView.getComboBoxOrarioChiusuraSerale().getSelectionModel().selectFirst();
        crudRestaurantView.getButtonCaricaFoto().setDisable(true);
        crudRestaurantView.getListViewFotoPath().setDisable(true);
        crudRestaurantView.getListViewFotoPath().getItems().clear();
        initializeChoiceBoxIndirizzo();
        initializeComboBoxOrariMattutini();
        initializeComboBoxOrariSerali();
        initializeTableViewTypeOfCuisine();
    }

    private void initializeChoiceBoxIndirizzo() {
        ObservableList<String> observableList = FXCollections.observableArrayList("Via", "Viale", "Vico", "Piazza", "Largo");
        crudRestaurantView.getChoiceBoxIndirizzo().setItems(observableList);
        crudRestaurantView.getChoiceBoxIndirizzo().getSelectionModel().selectFirst();
    }

    private void initializeComboBoxOrariMattutini() {
        ObservableList<String> orariMattutini = FXCollections.observableArrayList(
                "07:00", "07:30", "08:00", "08:30", "09:00", "09:30", "10:00", "10:30", "11:00",
                "11:30", "12:00", "12:30", "13:00", "13.30", "14:00", "14:30");
        crudRestaurantView.getComboBoxOrarioAperturaMattutina().setItems(orariMattutini);
        crudRestaurantView.getComboBoxOrarioChiusuraMattutina().setItems(orariMattutini);
    }

    private void initializeComboBoxOrariSerali() {
        ObservableList<String> orariSerali = FXCollections.observableArrayList(
                "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00",
                "19:30", "20:00", "20:30", "21:00", "21.30", "22:00", "22:30",
                "23:00", "23.30", "00:00", "00:30", "01:00", "01:30", "02:00", "02.30", "03:00");
        crudRestaurantView.getComboBoxOrarioAperturaSerale().setItems(orariSerali);
        crudRestaurantView.getComboBoxOrarioChiusuraSerale().setItems(orariSerali);
    }

    private void initializeTableViewTypeOfCuisine() {
        crudRestaurantView.getTableViewTypeOfCuisine().setDisable(true);
        ObservableList<TableSettersGetters> list = FXCollections.observableArrayList();
        list.add(new TableSettersGetters("Mediterranea", new CheckBox()));
        list.add(new TableSettersGetters("Pizzeria", new CheckBox()));
        list.add(new TableSettersGetters("Ristorante", new CheckBox()));
        list.add(new TableSettersGetters("Rosticceria", new CheckBox()));
        list.add(new TableSettersGetters("Vegana", new CheckBox()));
        crudRestaurantView.getTableViewTypeOfCuisine().setItems(list);
        crudRestaurantView.getTypeOfCuisineNameColumn().setCellValueFactory(new PropertyValueFactory<>("name"));
        crudRestaurantView.getTypeOfCuisineSelectColumn().setCellValueFactory(new PropertyValueFactory<>("checkBox"));
    }

    public void showSelectTypeStage() {
        try {
            closeCurrentStage();
            Parent parent = FXMLLoader.load(getClass().getResource("/select_type.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeCurrentStage() {
        Stage thisStage = (Stage) crudRestaurantView.getRootPane().getScene().getWindow();
        thisStage.close();
    }
}