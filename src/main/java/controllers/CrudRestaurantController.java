package controllers;

import controllers_utils.CrudDialoger;
import controllers_utils.InputValidator;
import controllers_utils.TypeOfCuisineItem;
import dao_interfaces.CityDAO;
import dao_interfaces.RestaurantDAO;
import factories.DAOFactory;
import factories.FormCheckerFactory;
import geocoding.Geocoder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
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

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public class CrudRestaurantController extends CrudController {
    private final CrudRestaurantView crudRestaurantView;
    private RestaurantDAO restaurantDAO;

    public CrudRestaurantController(CrudRestaurantView crudRestaurantView) {
        this.crudRestaurantView = crudRestaurantView;
    }

    @Override
    public Stage getStage() {
        return (Stage) this.getCrudRestaurantView().getRootPane().getScene().getWindow();
    }

    @Override
    public void setListenerOnTableView(TableView<Object> tableView) {
        if (tableView.getId().equals("tableView")) {
            tableViewClicked();
        }
    }

    @Override
    public void setListenerOnListView(ListView<String> listViewFotoPath) {
        if (listViewFotoPath.getId().equals("listViewFotoPath")) {
            listViewFotoPathClicked();
        }
    }

    @Override
    public void setListenerOn(Button button) {
        switch (button.getId()) {
            case "buttonIndietro":
                buttonIndietroClicked();
                break;
            case "buttonInserisci":
                buttonInserisciClicked();
                break;
            case "buttonModifica":
                buttonModificaClicked();
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
            case "buttonEliminaFotoSelezionate":
                buttonEliminaFotoSelezionataClicked();
                break;
            case "buttonMostraIndietro":
                buttonMostraIndietroClicked();
                break;
            case "buttonMostraAvanti":
                buttonMostraAvantiClicked();
                break;
            case "buttonCerca":
                buttonCercaClicked();
                break;
        }
    }

    @Override
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
        crudRestaurantView.getTextFieldCity().setDisable(true);
        crudRestaurantView.getCheckBoxCertificatoDiEccellenza().setDisable(true);
        crudRestaurantView.getTableViewTypeOfCuisine().setDisable(true);
        crudRestaurantView.getTextFieldOpeningTime().setDisable(true);
        crudRestaurantView.getButtonCaricaFoto().setDisable(true);
        crudRestaurantView.getListViewFotoPath().setDisable(true);
        crudRestaurantView.getListViewFotoPath().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE); // multi select
        crudRestaurantView.getListViewFotoPath().getItems().clear();
        crudRestaurantView.getButtonEliminaFotoSelezionate().setDisable(true);
        crudRestaurantView.getTableView().setDisable(false);
        crudRestaurantView.getButtonCerca().setDisable(false);
        crudRestaurantView.getCheckBoxCertificatoDiEccellenza().setSelected(false);
        initializeBoxes();
        clearTextFields();
        loadRestaurantsIntoTableView(currentPage, currentPageSize);
    }

    private void buttonCercaClicked() {
        crudRestaurantView.getButtonCerca().setOnAction(event -> {
            retrieving = true;
            enableAllTextFields();
            enableAllChoiceBoxes();
            disableCRUDButtons();
            clearTextFields();
            crudRestaurantView.getTableViewTypeOfCuisine().setDisable(false);
            crudRestaurantView.getTableView().setDisable(true);
            crudRestaurantView.getButtonConferma().setDisable(false);
            crudRestaurantView.getButtonAnnulla().setDisable(false);
            crudRestaurantView.getButtonIndietro().setDisable(true);
            crudRestaurantView.getButtonCaricaFoto().setDisable(false);
            crudRestaurantView.getTextFieldNumeroDiTelefono().setDisable(false);
            crudRestaurantView.getListViewFotoPath().setDisable(false);
            crudRestaurantView.getButtonCaricaFoto().setDisable(true);
            crudRestaurantView.getListViewFotoPath().setDisable(true);
        });
    }

    private void listViewFotoPathClicked() {
        crudRestaurantView.getListViewFotoPath().setOnMouseClicked(event -> {
            if (!crudRestaurantView.getListViewFotoPath().getSelectionModel().getSelectedItems().isEmpty())
                crudRestaurantView.getButtonEliminaFotoSelezionate().setDisable(false);
        });
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
        getStage().close();
    }

    public void buttonInserisciClicked() {
        crudRestaurantView.getButtonInserisci().setOnAction(event -> {
            enableAllTextFields();
            enableAllChoiceBoxes();
            disableCRUDButtons();
            crudRestaurantView.getTableView().setDisable(true);
            crudRestaurantView.getButtonConferma().setDisable(false);
            crudRestaurantView.getButtonAnnulla().setDisable(false);
            crudRestaurantView.getButtonIndietro().setDisable(true);
            crudRestaurantView.getTableViewTypeOfCuisine().setDisable(false);
            crudRestaurantView.getButtonCaricaFoto().setDisable(false);
            crudRestaurantView.getTextFieldNumeroDiTelefono().setDisable(false);
            crudRestaurantView.getListViewFotoPath().setDisable(false);
        });
    }

    public void enableAllTextFields() {
        crudRestaurantView.getTextFieldOpeningTime().setDisable(false);
        crudRestaurantView.getTextFieldCAP().setDisable(false);
        crudRestaurantView.getTextFieldCity().setDisable(false);
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
        crudRestaurantView.getButtonCerca().setDisable(true);
        crudRestaurantView.getButtonInserisci().setDisable(true);
        crudRestaurantView.getButtonModifica().setDisable(true);
        crudRestaurantView.getButtonElimina().setDisable(true);
    }

    public void buttonModificaClicked() {
        crudRestaurantView.getButtonModifica().setOnAction(event -> {
            modifying = true;
            retrieving = false;
            crudRestaurantView.getButtonInserisci().setDisable(true);
            crudRestaurantView.getButtonConferma().setDisable(false);
            crudRestaurantView.getButtonAnnulla().setDisable(false);
            crudRestaurantView.getButtonCerca().setDisable(true);
            crudRestaurantView.getButtonModifica().setDisable(true);
            enableAllTextFields();
            enableAllChoiceBoxes();
            crudRestaurantView.getButtonCaricaFoto().setDisable(false);
            crudRestaurantView.getListViewFotoPath().setDisable(false);
            crudRestaurantView.getTableViewTypeOfCuisine().setDisable(false);
        });
    }

    public void buttonEliminaClicked() {
        crudRestaurantView.getButtonElimina().setOnAction(event -> {
            daoFactory = DAOFactory.getInstance();
            restaurantDAO = daoFactory.getRestaurantDAO(ConfigFileReader.getProperty("restaurant_storage_technology"));
            Restaurant selectedRestaurant = getSelectedRestaurantFromTableView();
            if (selectedRestaurant != null) {
                if (CrudDialoger.areYouSureToDelete(selectedRestaurant.getName())) {
                    try {
                        if (!restaurantDAO.delete(selectedRestaurant))
                            CrudDialoger.showAlertDialog("Qualcosa è andato storto durante la cancellazione");
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            setViewsAsDefault();
        });
    }

    private void initializeBoxes() {
        initializeChoiceBoxIndirizzo();
        initializeTableViewTypeOfCuisine();
    }

    private void clearTextFields() {
        crudRestaurantView.getTextFieldNome().setText("");
        crudRestaurantView.getTextFieldCAP().setText("");
        crudRestaurantView.getTextFieldCity().setText("");
        crudRestaurantView.getTextFieldPrezzoMedio().setText("");
        crudRestaurantView.getTextFieldNumeroDiTelefono().setText("");
        crudRestaurantView.getTextFieldStrada().setText("");
        crudRestaurantView.getTxtFieldNumeroCivico().setText("");
        crudRestaurantView.getTextFieldProvincia().setText("");
        crudRestaurantView.getTextFieldOpeningTime().setText("");
    }

    private void initializeChoiceBoxIndirizzo() {
        ObservableList<String> observableList = FXCollections.observableArrayList("Via", "Viale", "Vico", "Piazza", "Largo");
        crudRestaurantView.getChoiceBoxIndirizzo().setItems(observableList);
    }

    private void initializeTableViewTypeOfCuisine() {
        crudRestaurantView.getTableViewTypeOfCuisine().setDisable(true);
        ObservableList<TypeOfCuisineItem> list = FXCollections.observableArrayList();
        list.add(new TypeOfCuisineItem("Mediterranea", new CheckBox()));
        list.add(new TypeOfCuisineItem("Pizzeria", new CheckBox()));
        list.add(new TypeOfCuisineItem("Ristorante", new CheckBox()));
        list.add(new TypeOfCuisineItem("Rosticceria", new CheckBox()));
        list.add(new TypeOfCuisineItem("Vegana", new CheckBox()));
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
            Restaurant selectedRestaurant = getSelectedRestaurantFromTableView();
            if (selectedRestaurant != null) {
                crudRestaurantView.getButtonAnnulla().setDisable(false);
                crudRestaurantView.getButtonElimina().setDisable(false);
                crudRestaurantView.getButtonModifica().setDisable(false);
                crudRestaurantView.getListViewFotoPath().getItems().clear();
                clearTypeOfCuisineCheckBox();
                populateTextFieldsWithSelectedRestaurantData(selectedRestaurant);
            }
        });
    }

    private void populateTextFieldsWithSelectedRestaurantData(Restaurant restaurant) {
        crudRestaurantView.getTextFieldNome().setText(restaurant.getName());
        crudRestaurantView.getTextFieldNumeroDiTelefono().setText(restaurant.getPhoneNumber());
        setProperAddressTypeIntoAddressTypeChoiceBox(restaurant);
        crudRestaurantView.getTextFieldStrada().setText(restaurant.getStreet());
        crudRestaurantView.getTxtFieldNumeroCivico().setText(restaurant.getHouseNumber());
        crudRestaurantView.getTextFieldCity().setText(restaurant.getCity());
        crudRestaurantView.getTextFieldCAP().setText(restaurant.getPostalCode());
        crudRestaurantView.getTextFieldProvincia().setText(restaurant.getProvince());
        crudRestaurantView.getTextFieldPrezzoMedio().setText(String.valueOf(restaurant.getAvaragePrice()));
        crudRestaurantView.getCheckBoxCertificatoDiEccellenza().setSelected(restaurant.isHasCertificateOfExcellence());
        setProperOpeningHourIntoCheckBox(restaurant);
        setProperImagesIntoListView(restaurant);
        setProperTypeOfCuisineIntoListView(restaurant);
    }

    private void setProperAddressTypeIntoAddressTypeChoiceBox(Restaurant restaurant) {
        if (restaurant.getTypeOfAddress().equals("Via"))
            crudRestaurantView.getChoiceBoxIndirizzo().getSelectionModel().select(0);
        if (restaurant.getTypeOfAddress().equals("Viale"))
            crudRestaurantView.getChoiceBoxIndirizzo().getSelectionModel().select(1);
        if (restaurant.getTypeOfAddress().equals("Vico"))
            crudRestaurantView.getChoiceBoxIndirizzo().getSelectionModel().select(2);
        if (restaurant.getTypeOfAddress().equals("Piazza"))
            crudRestaurantView.getChoiceBoxIndirizzo().getSelectionModel().select(3);
        if (restaurant.getTypeOfAddress().equals("Largo"))
            crudRestaurantView.getChoiceBoxIndirizzo().getSelectionModel().select(4);
    }

    private void setProperImagesIntoListView(Restaurant restaurant) {
        if (restaurant.getImages() != null) {
            for (String image : restaurant.getImages()) {
                crudRestaurantView.getListViewFotoPath().getItems().add(image);
            }
        }
    }

    private void setProperTypeOfCuisineIntoListView(Restaurant restaurant) {
        List<String> typesOfCuisine = restaurant.getTypeOfCuisine();
        if (typesOfCuisine != null) {
            for (String type : typesOfCuisine) {
                for (TypeOfCuisineItem typeOfCuisineItem : getTypesOfCuisine()) {
                    if (typeOfCuisineItem.getName().equals(type))
                        typeOfCuisineItem.getCheckBox().setSelected(true);
                }
            }
        }
    }

    private void setProperOpeningHourIntoCheckBox(Restaurant restaurant) {
        crudRestaurantView.getTextFieldOpeningTime().setText(restaurant.getOpeningTime());
    }

    private void clearTypeOfCuisineCheckBox() {
        for (TypeOfCuisineItem typeOfCuisineItem : crudRestaurantView.getTableViewTypeOfCuisine().getItems())
            typeOfCuisineItem.getCheckBox().setSelected(false);
    }

    private void buttonMostraAvantiClicked() {
        crudRestaurantView.getButtonMostraAvanti().setOnAction(event -> {
            if (!crudRestaurantView.getTableView().getItems().isEmpty())
                loadRestaurantsIntoTableView(++currentPage, currentPageSize);
        });
    }

    private void buttonMostraIndietroClicked() {
        crudRestaurantView.getButtonMostraIndietro().setOnAction(event -> {
            if (currentPage != 0)
                loadRestaurantsIntoTableView(--currentPage, currentPageSize);
        });
    }

    private void buttonConfermaClicked() {
        crudRestaurantView.getButtonConferma().setOnAction(event -> {
            String telephoneNumber = getNumeroDiTelefono();
            String prezzoMedio = getPrezzoMedio();
            String numeroCivico = getNumeroCivico();
            String cap = getCAP();
            String openingTime = getOpeningTimeWithFormData();

            formCheckerFactory = FormCheckerFactory.getInstance();
            formChecker = formCheckerFactory.getFormChecker(this);

            if (retrieving) {
                try {
                    doRetrieveByQuery();
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                if (formChecker.formHasSomeEmptyField(this)) {
                    CrudDialoger.showAlertDialog("Riempi tutti i campi");
                } else {
                    if (InputValidator.isValidTelephoneNumber(telephoneNumber)) {
                        if (InputValidator.isNumberGreaterOrEqualToZero(prezzoMedio)) {
                            if (InputValidator.isNumberGreaterOrEqualToZero(numeroCivico)) {
                                if (InputValidator.isNumberGreaterOrEqualToZero(cap)) {
                                    if (InputValidator.isValidOpeningTime(openingTime)) {
                                        Restaurant restaurant = getRestaurantWithFormData();
                                        daoFactory = DAOFactory.getInstance();
                                        restaurantDAO = daoFactory.getRestaurantDAO(ConfigFileReader.getProperty("restaurant_storage_technology"));
                                        if (modifying) {
                                            doUpdate(restaurant);
                                            imagesSelectedToDelete = null;
                                        } else
                                            doInsert(restaurant);
                                    } else {
                                        CrudDialoger.showAlertDialog("Orario non valido");
                                    }
                                } else {
                                    CrudDialoger.showAlertDialog("CAP non valido");
                                }
                            } else {
                                CrudDialoger.showAlertDialog("Numero civico non valido");
                            }
                        } else {
                            CrudDialoger.showAlertDialog("Prezzo medio non valido. Inserire un intero");
                        }
                    } else {
                        CrudDialoger.showAlertDialog("Numero di telefono non valido");
                    }
                }
            }
        });
    }

    private void doRetrieveByQuery() throws IOException, InterruptedException {
        String query = getQuery();

        CrudDialoger.showAlertDialog(query); // dbg

        daoFactory = DAOFactory.getInstance();
        restaurantDAO = daoFactory.getRestaurantDAO(ConfigFileReader.getProperty("restaurant_storage_technology"));

        List<Restaurant> restaurants = (query.equals("")) ? restaurantDAO.retrieveAt(currentPage, currentPageSize)
                : restaurantDAO.retrieveByQuery(query, currentPage, currentPageSize);

        if (restaurants != null) {
            List<String> typeOfCuisineDesired = getTypeOfCuisineWithFormData();
            restaurants.removeIf(restaurant -> !restaurant.getTypeOfCuisine().containsAll(typeOfCuisineDesired));
            String openingTimeDesired = getOpeningTimeWithFormData();
            if (!openingTimeDesired.isEmpty())
                restaurants.removeIf(restaurant -> !restaurant.getOpeningTime().equals(openingTimeDesired));
            final ObservableList<Object> data = FXCollections.observableArrayList(restaurants);
            fillColumnsWithData();
            crudRestaurantView.getTableView().setItems(data);
            crudRestaurantView.getTableView().setDisable(false);
        } else {
            CrudDialoger.showAlertDialog("Non sono stati trovati ristoranti con questi criteri: " + query +
                    "&page=" + currentPage + "&size=" + currentPageSize);
        }
        disableCRUDButtons();
    }

    private String getQuery() {
        String query = "";
        final String nome = getNome();
        final String numeroDiTelefono = getNumeroDiTelefono();
        final String tipoIndirizzo = getTipoIndirizzo();
        final String strada = getStrada();
        final String civico = getNumeroCivico();
        final String city = getCity();
        final String cap = getCAP();
        final String provincia = getProvincia();
        final String prezzoMedio = getPrezzoMedio();
        String certificatoDiEccellenza = String.valueOf(crudRestaurantView.getCheckBoxCertificatoDiEccellenza().isSelected());
        boolean concatena = false;

        if (!nome.isEmpty()) {
            query += "name==\"" + nome + "\"";
            concatena = true;
        }

        if (!numeroDiTelefono.isEmpty()) {
            if (concatena)
                query += ";phoneNumber==\"" + numeroDiTelefono + "\"";
            else {
                query += "phoneNumber==\"" + numeroDiTelefono + "\"";
                concatena = true;
            }
        }
        if (tipoIndirizzo != null) {
            if (concatena)
                query += ";address.type==\"" + tipoIndirizzo + "\"";
            else {
                query += "address.type==\"" + tipoIndirizzo + "\"";
                concatena = true;
            }
        }
        if (!strada.isEmpty()) {
            if (concatena)
                query += ";address.street==\"" + strada + "\"";
            else {
                query += "address.street==\"" + strada + "\"";
                concatena = true;
            }
        }
        if (!civico.isEmpty()) {
            if (concatena)
                query += ";address.houseNumber==\"" + civico + "\"";
            else {
                query += "address.houseNumber==\"" + civico + "\"";
                concatena = true;
            }
        }
        if (!city.isEmpty()) {
            if (concatena)
                query += ";address.city==\"" + city + "\"";
            else {
                query += "address.city==\"" + city + "\"";
                concatena = true;
            }
        }
        if (!cap.isEmpty()) {
            if (concatena)
                query += ";address.postalCode==\"" + cap + "\"";
            else {
                query += "address.postalCode==\"" + cap + "\"";
                concatena = true;
            }
        }
        if (!provincia.isEmpty()) {
            if (concatena)
                query += ";address.province==\"" + provincia + "\"";
            else {
                query += "address.province==\"" + provincia + "\"";
                concatena = true;
            }
        }
        if (!prezzoMedio.isEmpty()) {
            if (concatena)
                query += ";avaragePrice==\"" + prezzoMedio + "\"";
            else {
                query += "avaragePrice==\"" + prezzoMedio + "\"";
                concatena = true;
            }
        }

        if (crudRestaurantView.getCheckBoxCertificatoDiEccellenza().isSelected()) {
            if (concatena)
                query += ";certificateOfExcellence==\"" + certificatoDiEccellenza + "\"";
            else
                query += "certificateOfExcellence==\"" + certificatoDiEccellenza + "\"";
        } else {
            if (!CrudDialoger.ignoreExcellence()) {
                certificatoDiEccellenza = "false";
                if (concatena)
                    query += ";certificateOfExcellence==\"" + certificatoDiEccellenza + "\"";
                else
                    query += "certificateOfExcellence==\"" + certificatoDiEccellenza + "\"";
            }
        }
        return query;
    }

    private void doInsert(Restaurant restaurant) {
        try {
            if (!restaurantDAO.add(restaurant))
                CrudDialoger.showAlertDialog("Qualcosa è andato storto durante l'inserimento");
            else
                CrudDialoger.showAlertDialog("Inserimento avvenuto");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        setViewsAsDefault();
    }

    private void doUpdate(Restaurant restaurant) {
        Restaurant clickedRestaurant = (Restaurant) crudRestaurantView.getTableView().getSelectionModel().getSelectedItem();
        restaurant.setId(clickedRestaurant.getId());
        restaurant.setReviews(clickedRestaurant.getReviews());
        restaurant.setLastModificationDate(clickedRestaurant.getLastModificationDate());

        //CrudDialoger.showAlertDialog(this, "Old City: " + clickedRestaurant.getCity() + "\nNew City: " + restaurant.getCity()); // dbg

        //CrudDialoger.showAlertDialog(this, "imagesSelectedToDelete: " + imagesSelectedToDelete); // dbg

        //System.out.println("Restaurant debug: " + restaurant); // dbg
        for (String image : getImagesFromListView()) {
            File file = new File(image);
            //CrudDialoger.showAlertDialog(this, image); // dbg
            if (file.isAbsolute()) {
                //File file = new File(image);
                CrudDialoger.showAlertDialog(image + " da inserire"); // dbg
                daoFactory = DAOFactory.getInstance();
                imageDAO = daoFactory.getImageDAO(ConfigFileReader.getProperty("image_storage_technology"));
                String imageHostUrl = null;
                try {
                    imageHostUrl = imageDAO.loadFileIntoBucket(file); // carica su s3
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    if (restaurantDAO.updateRestaurantSingleImageFromRestaurantCollection(restaurant, imageHostUrl) || imageHostUrl == null)
                        CrudDialoger.showAlertDialog("Qualcosa è andato storto");
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            if (!restaurantDAO.update(restaurant))
                CrudDialoger.showAlertDialog("Qualcosa è andato storto durante l'update di restaurant");
            else
                updateImages(restaurant, imagesSelectedToDelete);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        if (!clickedRestaurant.getCity().equals(restaurant.getCity())) {
            //CrudDialoger.showAlertDialog(this, clickedRestaurant.getCity() + " not equal to " + restaurant.getCity());
            daoFactory = DAOFactory.getInstance();
            CityDAO cityDAO = daoFactory.getCityDAO(ConfigFileReader.getProperty("city_storage_technology"));
            try {
                //System.out.println(clickedRestaurant);
                if (!cityDAO.delete(clickedRestaurant))
                    CrudDialoger.showAlertDialog("Non è stato possibile eliminare"); // dbg
                if (!cityDAO.insert(restaurant))
                    CrudDialoger.showAlertDialog("Non è stato possibile inserire"); // dbg
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        setViewsAsDefault();
    }

    private void updateImages(Restaurant restaurant, ObservableList<String> images) throws IOException, InterruptedException {
        daoFactory = DAOFactory.getInstance();
        imageDAO = daoFactory.getImageDAO(ConfigFileReader.getProperty("image_storage_technology"));
        restaurantDAO = daoFactory.getRestaurantDAO(ConfigFileReader.getProperty("restaurant_storage_technology"));
        if (images != null) {
            for (String imageUrl : images) {
                if (!imageDAO.deleteThisImageFromBucket(imageUrl) || !restaurantDAO.deleteRestaurantSingleImageFromRestaurantCollection(restaurant, imageUrl))
                    CrudDialoger.showAlertDialog("Modifica non avvenuta");
            }
            //CrudDialoger.showAlertDialog(this, "Modifica effettuata");
        }
    }

    private void buttonAiutoClicked() {
        crudRestaurantView.getButtonAiuto().setOnAction(event -> CrudDialoger.showHelpDialog());
    }

    private void buttonCaricaClicked() {
        crudRestaurantView.getButtonCaricaFoto().setOnAction(event -> multiFileSelectionFromFileSystem());
    }

    public void multiFileSelectionFromFileSystem() {
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
            CrudDialoger.showAlertDialog("I file selezionati non sono validi");
        }
    }

    private void buttonEliminaFotoSelezionataClicked() {
        crudRestaurantView.getButtonEliminaFotoSelezionate().setOnAction(event -> {
            ObservableList<String> imagesSelectedToDelete2 = crudRestaurantView.getListViewFotoPath().getSelectionModel().getSelectedItems();
            imagesSelectedToDelete = FXCollections.observableArrayList(imagesSelectedToDelete2);
            crudRestaurantView.getListViewFotoPath().getItems().removeAll(imagesSelectedToDelete2);
        });
    }

    private void buttonAnnullaClicked() {
        crudRestaurantView.getButtonAnnulla().setOnAction(event -> {
            setViewsAsDefault();
            retrieving = false;
            modifying = false;
        });
    }

    private Restaurant getRestaurantWithFormData() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(crudRestaurantView.getTextFieldNome().getText());
        restaurant.setAddress(getAddressWithFormData());
        restaurant.setAvaragePrice(Integer.parseInt(crudRestaurantView.getTextFieldPrezzoMedio().getText()));
        restaurant.setPhoneNumber(crudRestaurantView.getTextFieldNumeroDiTelefono().getText());
        restaurant.setHasCertificateOfExcellence(crudRestaurantView.getCheckBoxCertificatoDiEccellenza().isSelected());
        restaurant.setImages(crudRestaurantView.getListViewFotoPath().getItems());
        restaurant.setTypeOfCuisine(getTypeOfCuisineWithFormData());
        restaurant.setPoint(new Point(Geocoder.reverseGeocoding(getEligibleStringAddressForGeocoding()).getLat(),
                Geocoder.reverseGeocoding(getEligibleStringAddressForGeocoding()).getLng()));
        restaurant.setAddedDate(getCurrentDate());
        restaurant.setOpeningTime(getOpeningTimeWithFormData());
        return restaurant;
    }

    private Address getAddressWithFormData() {
        return new Address(
                crudRestaurantView.getChoiceBoxIndirizzo().getValue(),
                crudRestaurantView.getTextFieldStrada().getText(),
                crudRestaurantView.getTxtFieldNumeroCivico().getText(),
                crudRestaurantView.getTextFieldCity().getText(),
                crudRestaurantView.getTextFieldProvincia().getText(),
                crudRestaurantView.getTextFieldCAP().getText());
    }

    private List<String> getTypeOfCuisineWithFormData() {
        List<String> cuisineSelected = new ArrayList<>();
        for (TypeOfCuisineItem typeOfCuisineItem : crudRestaurantView.getTableViewTypeOfCuisine().getItems()) {
            if (typeOfCuisineItem.getCheckBox().isSelected())
                cuisineSelected.add(typeOfCuisineItem.getName());
        }
        return cuisineSelected;
    }

    private String getEligibleStringAddressForGeocoding() {
        return crudRestaurantView.getChoiceBoxIndirizzo().getValue() + " " + crudRestaurantView.getTextFieldStrada().getText() + ", " +
                crudRestaurantView.getTxtFieldNumeroCivico().getText() + ", " + crudRestaurantView.getTextFieldCity().getText() + ", " + crudRestaurantView.getTextFieldCAP().getText() +
                ", " + crudRestaurantView.getTextFieldProvincia().getText();
    }

    private String getCurrentDate() {
        return new Date().toString();
    }

    public String getTipoIndirizzo() {
        return this.crudRestaurantView.getChoiceBoxIndirizzo().getValue();
    }

    private String getOpeningTimeWithFormData() {
        return crudRestaurantView.getTextFieldOpeningTime().getText();
    }

    public CrudRestaurantView getCrudRestaurantView() {
        return crudRestaurantView;
    }

    public String getNome() {
        return this.crudRestaurantView.getTextFieldNome().getText();
    }

    public String getStrada() {
        return this.crudRestaurantView.getTextFieldStrada().getText();
    }

    public String getNumeroCivico() {
        return this.crudRestaurantView.getTxtFieldNumeroCivico().getText();
    }

    public String getProvincia() {
        return this.crudRestaurantView.getTextFieldProvincia().getText();
    }

    public String getCAP() {
        return this.crudRestaurantView.getTextFieldCAP().getText();
    }

    public String getCity() {
        return this.crudRestaurantView.getTextFieldCity().getText();
    }

    public String getPrezzoMedio() {
        return this.crudRestaurantView.getTextFieldPrezzoMedio().getText();
    }

    public String getNumeroDiTelefono() {
        return this.crudRestaurantView.getTextFieldNumeroDiTelefono().getText();
    }

    public ObservableList<String> getImagesFromListView() {
        return this.getCrudRestaurantView().getListViewFotoPath().getItems();
    }

    public ObservableList<TypeOfCuisineItem> getTypesOfCuisine() {
        return this.getCrudRestaurantView().getTableViewTypeOfCuisine().getItems();
    }

    public String getOpeningTime() {
        return this.crudRestaurantView.getTextFieldOpeningTime().getText();
    }

    public Restaurant getSelectedRestaurantFromTableView() {
        return (Restaurant) crudRestaurantView.getTableView().getSelectionModel().getSelectedItem();
    }
}