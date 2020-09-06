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
import javafx.scene.control.*;
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
    private final int currentSize = 100;
    private int currentPage = 0;

    public CrudRestaurantController(CrudRestaurantView crudRestaurantView) {
        this.crudRestaurantView = crudRestaurantView;
    }

    public void setListenerOnTableView(TableView<Object> tableView) {
        if ("tableView".equals(tableView.getId())) {
            tableViewClicked();
        }
    }

    public void setListenerOn(Button button) {
        switch (button.getId()) {
            case "buttonIndietro":
                buttonIndietroClicked();
                break;
            case "buttonInserisci":
                buttonInserisciClicked();
                break;
            case "buttonElimina":
                buttonEliminaClicked();
                break;
            case "buttonConferma":
                buttonConfermaClicked();
                break;
            case "buttonAnnulla":
                buttonAnnullaClicked();
                break;
            case "buttonAiuto":
                buttonAiutoClicked();
                break;
            case "buttonCaricaFoto":
                buttonCaricaClicked();
                break;
            case "buttonMostraIndietro":
                buttonMostraIndietroClicked();
                break;
            case "buttonMostraAvanti":
                buttonMostraAvantiClicked();
                break;
        }
    }

    public void buttonIndietroClicked() {
        crudRestaurantView.getButtonIndietro().setOnAction(event -> showSelectTypeStage());
    }

    public void showSelectTypeStage() {
        try {
            closeCurrentStage();
            Parent parent = FXMLLoader.load(getClass().getResource("/select_collection_type.fxml"));
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

    public void buttonInserisciClicked() {
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

    public void enableAllTextFields() {
        crudRestaurantView.getTextFieldCAP().setDisable(false);
        crudRestaurantView.getTextFieldCittà().setDisable(false);
        crudRestaurantView.getTextFieldNome().setDisable(false);
        crudRestaurantView.getTextFieldNumeroDiTelefono().setDisable(false);
        crudRestaurantView.getTextFieldPrezzoMedio().setDisable(false);
        crudRestaurantView.getTextFieldStrada().setDisable(false);
        crudRestaurantView.getTxtFieldNumeroCivico().setDisable(false);
        crudRestaurantView.getTextFieldProvincia().setDisable(false);
    }

    public void enableAllChoiceBoxes() {
        crudRestaurantView.getCheckBoxCertificatoDiEccellenza().setDisable(false);
        crudRestaurantView.getChoiceBoxIndirizzo().setDisable(false);
    }

    public void disableCRUDButtons() {
        crudRestaurantView.getButtonInserisci().setDisable(true);
        crudRestaurantView.getButtonModifica().setDisable(true);
        crudRestaurantView.getButtonElimina().setDisable(true);
    }

    public void buttonEliminaClicked() {
        crudRestaurantView.getButtonElimina().setOnAction(event -> {
            daoFactory = DAOFactory.getInstance();
            restaurantDAO = daoFactory.getRestaurantDAO(ConfigFileReader.getProperty("restaurant_storage_technology"));
            Restaurant clickedRestaurant = (Restaurant) crudRestaurantView.getTableView().getSelectionModel().getSelectedItem();
            if (clickedRestaurant != null) {
                if (areYouSureDialog(clickedRestaurant)) {
                    try {
                        if (!restaurantDAO.delete(clickedRestaurant))
                            showAlertDialog("Qualcosa è andato storto durante la cancellazione");
                        else
                            setViewsAsDefault();
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private boolean areYouSureDialog(Restaurant restaurant) {
        Stage stage = (Stage) crudRestaurantView.getRootPane().getScene().getWindow();
        Alert.AlertType alertType = Alert.AlertType.CONFIRMATION;
        Alert alert = new Alert(alertType, "");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(stage);
        alert.setTitle("Attenzione");
        alert.getDialogPane().setHeaderText("Sei sicuro di voler cancellare " + restaurant.getName() + " ?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == ButtonType.OK) {
                alert.close();
                return true;
            }
        }
        return false;
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
        loadRestaurantsIntoTableView(currentPage, currentSize);
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
                "19:30", "20:00", "20:30", "21:00", "21:30", "22:00", "22:30",
                "23:00", "23:30", "00:00", "00:30", "01:00", "01:30", "02:00", "02:30", "03:00");
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

    public void loadRestaurantsIntoTableView(int page, int size) {
        daoFactory = DAOFactory.getInstance();
        restaurantDAO = daoFactory.getRestaurantDAO(ConfigFileReader.getProperty("restaurant_storage_technology"));
        try {
            List<Restaurant> restaurants = restaurantDAO.retrieveAt(page, size);
            if (restaurants != null) {
                final ObservableList<Object> data = FXCollections.observableArrayList(restaurants);
                fillColumnsWithData();
                // Associa colonne alla tabella
                crudRestaurantView.getTableView().setItems(data);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void fillColumnsWithData() {
        crudRestaurantView.getTableColumnId().setCellValueFactory(new PropertyValueFactory<>("id"));
        crudRestaurantView.getTableColumnName().setCellValueFactory(new PropertyValueFactory<>("name"));
        crudRestaurantView.getTableColumnOrarioApertura().setCellValueFactory(new PropertyValueFactory<>("openingTime"));
        crudRestaurantView.getTableColumnVotoMedio().setCellValueFactory(new PropertyValueFactory<>("avarageRating"));
        crudRestaurantView.getTableColumnPrezzoMedio().setCellValueFactory(new PropertyValueFactory<>("avaragePrice"));
        crudRestaurantView.getTableColumnNumeroDiTelefono().setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        crudRestaurantView.getTableColumnTotalReview().setCellValueFactory(new PropertyValueFactory<>("totalReviews"));
        crudRestaurantView.getTableColumnHasCertificateOfExcellence().setCellValueFactory(new PropertyValueFactory<>("hasCertificateOfExcellence"));
        crudRestaurantView.getTableColumnAddedDate().setCellValueFactory(new PropertyValueFactory<>("addedDate"));
        crudRestaurantView.getTableColumnLastModificationDate().setCellValueFactory(new PropertyValueFactory<>("lastModificationDate"));
        crudRestaurantView.getTableColumnIndirizzo().setCellValueFactory(new PropertyValueFactory<>("address"));
        crudRestaurantView.getTableColumnTipoDiCucina().setCellValueFactory(new PropertyValueFactory<>("typeOfCuisine"));
        crudRestaurantView.getTableColumnPuntoSuMappa().setCellValueFactory(new PropertyValueFactory<>("point"));
        crudRestaurantView.getTableColumnImmagini().setCellValueFactory(new PropertyValueFactory<>("images"));
    }

    private void tableViewClicked() {
        crudRestaurantView.getTableView().setOnMouseClicked(event -> {
            Restaurant clickedRestaurant = (Restaurant) crudRestaurantView.getTableView().getSelectionModel().getSelectedItem();
            if (clickedRestaurant != null) {
                crudRestaurantView.getButtonElimina().setDisable(false);
                crudRestaurantView.getListViewFotoPath().getItems().clear();
                clearTypeOfCuisineCheckBox();
                populateTextFieldWithClickedRestaurant(clickedRestaurant);
            }
        });
    }

    private void populateTextFieldWithClickedRestaurant(Restaurant restaurant) {
        crudRestaurantView.getTextFieldNome().setText(restaurant.getName());
        crudRestaurantView.getTextFieldNumeroDiTelefono().setText(restaurant.getPhoneNumber());
        setProperAddressTypeIntoAddressTypeChoiceBox(restaurant);
        crudRestaurantView.getTextFieldStrada().setText(restaurant.getStreet());
        crudRestaurantView.getTxtFieldNumeroCivico().setText(restaurant.getHouseNumber());
        crudRestaurantView.getTextFieldCittà().setText(restaurant.getCity());
        crudRestaurantView.getTextFieldCAP().setText(restaurant.getPostalCode());
        crudRestaurantView.getTextFieldProvincia().setText(restaurant.getProvince());
        crudRestaurantView.getTextFieldPrezzoMedio().setText(String.valueOf(restaurant.getAvaragePrice()));
        crudRestaurantView.getCheckBoxCertificatoDiEccellenza().setDisable(!restaurant.isHasCertificateOfExcellence());
        setProperOpeningHourIntoCheckBox(restaurant);
        setProperImagesIntoListViewPhotoPath(restaurant);
        setProperTypeOfCuisineIntoListView(restaurant);
    }

    private void setProperAddressTypeIntoAddressTypeChoiceBox(Restaurant restaurant) {
        if (restaurant.getTypeOfAddress().equals("Via"))
            crudRestaurantView.getChoiceBoxIndirizzo().getSelectionModel().select(0);
        if (restaurant.getTypeOfAddress().equals("Vie"))
            crudRestaurantView.getChoiceBoxIndirizzo().getSelectionModel().select(1);
        if (restaurant.getTypeOfAddress().equals("Vico"))
            crudRestaurantView.getChoiceBoxIndirizzo().getSelectionModel().select(2);
        if (restaurant.getTypeOfAddress().equals("Piazza"))
            crudRestaurantView.getChoiceBoxIndirizzo().getSelectionModel().select(3);
        if (restaurant.getTypeOfAddress().equals("Largo"))
            crudRestaurantView.getChoiceBoxIndirizzo().getSelectionModel().select(4);
    }

    private void setProperImagesIntoListViewPhotoPath(Restaurant restaurant) {
        if (restaurant.getImages() != null) {
            for (String image : restaurant.getImages()) {
                crudRestaurantView.getListViewFotoPath().getItems().add(image);
            }
        }
    }

    private void setProperTypeOfCuisineIntoListView(Restaurant restaurant) {
        List<String> typeOfCuisine = restaurant.getTypeOfCuisine();
        for (String type : typeOfCuisine) {
            for (TableSettersGetters tableSettersGetters : crudRestaurantView.getTableViewTypeOfCuisine().getItems()) {
                if (tableSettersGetters.getName().equals(type))
                    tableSettersGetters.getCheckBox().setSelected(true);
            }
        }
    }

    private void setProperOpeningHourIntoCheckBox(Restaurant restaurant) {
        String aperturaMattutina = restaurant.getOpeningTime().substring(0, 5);
        String chiusuraMattutina = restaurant.getOpeningTime().substring(8, 13);
        String aperturaSerale = restaurant.getOpeningTime().substring(14, 19);
        String chiusuraSerale = restaurant.getOpeningTime().substring(21, 27);
        crudRestaurantView.getComboBoxOrarioAperturaMattutina().getSelectionModel().select(aperturaMattutina);
        crudRestaurantView.getComboBoxOrarioChiusuraMattutina().getSelectionModel().select(chiusuraMattutina);
        crudRestaurantView.getComboBoxOrarioAperturaSerale().getSelectionModel().select(aperturaSerale);
        crudRestaurantView.getComboBoxOrarioChiusuraSerale().getSelectionModel().select(chiusuraSerale);
    }

    private void clearTypeOfCuisineCheckBox() {
        for (TableSettersGetters tableSettersGetters : crudRestaurantView.getTableViewTypeOfCuisine().getItems())
            tableSettersGetters.getCheckBox().setSelected(false);
    }

    private void buttonMostraAvantiClicked() {
        crudRestaurantView.getButtonMostraAvanti().setOnAction(event -> {
            System.out.println("Premuto Mostra avanti");
            if (!crudRestaurantView.getTableView().getItems().isEmpty()) {
                currentPage++;
                loadRestaurantsIntoTableView(currentPage, currentSize);
            }
        });
    }

    private void buttonMostraIndietroClicked() {
        crudRestaurantView.getButtonMostraIndietro().setOnAction(event -> {
            System.out.println("Premuto mostra indietro");
            if (currentPage != 0) {
                currentPage--;
                loadRestaurantsIntoTableView(currentPage, currentSize);
            }
        });
    }

    private void buttonConfermaClicked() {
        crudRestaurantView.getButtonConferma().setOnAction(event -> {
            String telephoneNumber = crudRestaurantView.getTextFieldNumeroDiTelefono().getText();
            String prezzoMedio = crudRestaurantView.getTextFieldPrezzoMedio().getText();
            String numeroCivico = crudRestaurantView.getTxtFieldNumeroCivico().getText();
            String cap = crudRestaurantView.getTextFieldCAP().getText();
            String orarioAperturaMattutina = crudRestaurantView.getComboBoxOrarioAperturaMattutina().getValue();
            String orarioChiusuraMattutina = crudRestaurantView.getComboBoxOrarioChiusuraMattutina().getValue();
            String orarioAperturaSerale = crudRestaurantView.getComboBoxOrarioAperturaSerale().getValue();
            String orarioChiusuraSerale = crudRestaurantView.getComboBoxOrarioChiusuraSerale().getValue();
            if (formHasSomeEmptyFields()) {
                showAlertDialog("Riempire tutti i campi");
            } else {
                if (UserInputChecker.isValidTelephoneNumber(telephoneNumber)) {
                    if (UserInputChecker.isNumberGreaterOrEqualToZero(prezzoMedio)) {
                        if (UserInputChecker.isNumberGreaterOrEqualToZero(numeroCivico)) {
                            if (UserInputChecker.isNumberGreaterOrEqualToZero(cap)) {
                                if (UserInputChecker.isValidOpeningTimeAtMorning(orarioAperturaMattutina, orarioChiusuraMattutina)) {
                                    if (UserInputChecker.isValidOpeningTimeAtEvening(orarioAperturaSerale, orarioChiusuraSerale)) {
                                        Restaurant restaurant = createRestaurantWithFormData();
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
                                        showAlertDialog("Orario serale non valido");
                                    }
                                } else {
                                    showAlertDialog("Orario mattutino non valido");
                                }
                            } else {
                                showAlertDialog("CAP non valido");
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

    private void buttonAnnullaClicked() {
        crudRestaurantView.getButtonAnnulla().setOnAction(event -> setViewsAsDefault());
    }
}