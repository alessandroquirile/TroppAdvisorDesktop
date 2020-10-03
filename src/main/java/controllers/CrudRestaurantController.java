package controllers;

import controllers_utils.Dialoger;
import controllers_utils.InputValidator;
import controllers_utils.TypeOfCuisineItem;
import dao_interfaces.CityDAO;
import dao_interfaces.RestaurantDAO;
import factories.DAOFactory;
import factories.FormCheckerFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.Accomodation;
import models.Restaurant;
import utils.ConfigFileReader;
import views.CrudRestaurantView;
import views.CrudView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
    public void setViewsAsDefault(CrudView crudView) {
        super.setViewsAsDefault(crudRestaurantView);
        crudRestaurantView.getTableViewTypeOfCuisine().setDisable(true);
        crudRestaurantView.getTextFieldOpeningTime().setDisable(true);
        loadRestaurantsIntoTableView(currentPage, currentPageSize);
    }

    @Override
    protected void buttonCercaClickedEvent(CrudView crudView) {
        super.buttonCercaClickedEvent(crudView);
        crudRestaurantView.getTableViewTypeOfCuisine().setDisable(false);
    }

    @Override
    protected void buttonInserisciClickedEvent(CrudView crudView) {
        super.buttonInserisciClickedEvent(crudView);
        crudRestaurantView.getTableViewTypeOfCuisine().setDisable(false);
    }

    @Override
    protected void enableAllTextFields(CrudView crudView) {
        super.enableAllTextFields(crudView);
        crudRestaurantView.getTextFieldOpeningTime().setDisable(false);
    }

    @Override
    protected void buttonEliminaClickedEvent() {
        daoFactory = DAOFactory.getInstance();
        restaurantDAO = daoFactory.getRestaurantDAO(ConfigFileReader.getProperty("restaurant_storage_technology"));
        Restaurant selectedRestaurant = (Restaurant) getSelectedAccomodationFromTableView(crudRestaurantView);
        if (selectedRestaurant != null) {
            if (Dialoger.areYouSureToDelete(selectedRestaurant.getName())) {
                try {
                    if (!restaurantDAO.delete(selectedRestaurant))
                        Dialoger.showAlertDialog("Qualcosa è andato storto durante la cancellazione");
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        setViewsAsDefault(crudRestaurantView);
    }

    @Override
    protected void initializeBoxes(CrudView crudView) {
        super.initializeBoxes(crudView);
        initializeTableViewTypeOfCuisine();
    }

    @Override
    protected void clearTextFields(CrudView crudView) {
        super.clearTextFields(crudView);
        crudRestaurantView.getTextFieldOpeningTime().setText("");
    }

    @Override
    protected void tableViewClickedEvent() {
        Restaurant selectedRestaurant = (Restaurant) getSelectedAccomodationFromTableView(crudRestaurantView);
        if (selectedRestaurant != null) {
            crudRestaurantView.getButtonAnnulla().setDisable(false);
            crudRestaurantView.getButtonElimina().setDisable(false);
            crudRestaurantView.getButtonModifica().setDisable(false);
            crudRestaurantView.getListViewFotoPath().getItems().clear();
            clearTypeOfCuisineCheckBox();
            populateTextFieldsWithSelectedAccomodationData(selectedRestaurant, crudRestaurantView);
        }
    }

    @Override
    protected void populateTextFieldsWithSelectedAccomodationData(Accomodation accomodation, CrudView crudView) {
        super.populateTextFieldsWithSelectedAccomodationData(accomodation, crudView);
        setProperOpeningHourIntoCheckBox((Restaurant) accomodation);
        setProperOpeningHourIntoCheckBox((Restaurant) accomodation);
        setProperTypeOfCuisineIntoListView((Restaurant) accomodation);
    }

    @Override
    protected void buttonMostraAvantiClickedEvent() {
        if (!crudRestaurantView.getTableView().getItems().isEmpty())
            loadRestaurantsIntoTableView(++currentPage, currentPageSize);
    }

    @Override
    protected void buttonMostraIndietroClickedEvent() {
        if (currentPage != 0)
            loadRestaurantsIntoTableView(--currentPage, currentPageSize);
    }

    @Override
    protected void buttonConfermaClickedEvent() {
        String telephoneNumber = getNumeroDiTelefono();
        String prezzoMedio = getPrezzoMedio();
        String cap = getCAP();
        String openingTime = getOpeningTimeByFormData();

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
                Dialoger.showAlertDialog("Riempi tutti i campi");
            } else {
                if (InputValidator.isValidTelephoneNumber(telephoneNumber)) {
                    if (InputValidator.isNumberGreaterOrEqualToZero(prezzoMedio)) {
                        if (InputValidator.isNumberGreaterOrEqualToZero(cap)) {
                            if (InputValidator.isValidOpeningTime(openingTime)) {
                                Restaurant restaurant = (Restaurant) getAccomodationByFormData(crudRestaurantView);
                                daoFactory = DAOFactory.getInstance();
                                restaurantDAO = daoFactory.getRestaurantDAO(ConfigFileReader.getProperty("restaurant_storage_technology"));
                                if (modifying) {
                                    doUpdate(restaurant);
                                    imagesSelectedToDelete = null;
                                } else
                                    doInsert(restaurant);
                            } else
                                Dialoger.showAlertDialog("Orario non valido");
                        } else
                            Dialoger.showAlertDialog("CAP non valido");
                    } else
                        Dialoger.showAlertDialog("Prezzo medio non valido. Inserire un intero");
                } else
                    Dialoger.showAlertDialog("Numero di telefono non valido");
            }
        }
    }

    private void doRetrieveByQuery() throws IOException, InterruptedException {
        String query = getQuery(crudRestaurantView);

        Dialoger.showAlertDialog(query); // dbg - attenzione non deve tener conto dei tipi di cucina (verrà tenuto conto dopo)

        daoFactory = DAOFactory.getInstance();
        restaurantDAO = daoFactory.getRestaurantDAO(ConfigFileReader.getProperty("restaurant_storage_technology"));

        List<Restaurant> restaurants = (query.equals("")) ? restaurantDAO.retrieveAt(currentPage, currentPageSize)
                : restaurantDAO.retrieveAt(query, currentPage, currentPageSize);

        if (restaurants != null) {
            List<String> typeOfCuisineDesired = getTypeOfCuisineByFormData();
            restaurants.removeIf(restaurant -> !restaurant.getTypeOfCuisine().containsAll(typeOfCuisineDesired));
            String openingTimeDesired = getOpeningTimeByFormData();
            if (!openingTimeDesired.isEmpty())
                restaurants.removeIf(restaurant -> !restaurant.getOpeningTime().equals(openingTimeDesired));
            final ObservableList<Object> data = FXCollections.observableArrayList(restaurants);
            fillColumnsWithData();
            crudRestaurantView.getTableView().setItems(data);
            crudRestaurantView.getTableView().setDisable(false);
        } else {
            Dialoger.showAlertDialog("Non sono stati trovati ristoranti con questi criteri: " + query +
                    "&page=" + currentPage + "&size=" + currentPageSize);
        }
        disableCRUDButtons(crudRestaurantView);
    }

    private void doInsert(Restaurant restaurant) {
        try {
            if (!restaurantDAO.add(restaurant))
                Dialoger.showAlertDialog("Qualcosa è andato storto durante l'inserimento");
            else
                Dialoger.showAlertDialog("Inserimento avvenuto");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        setViewsAsDefault(crudRestaurantView);
    }

    private void doUpdate(Restaurant restaurant) {
        Restaurant clickedRestaurant = (Restaurant) crudRestaurantView.getTableView().getSelectionModel().getSelectedItem();
        restaurant.setId(clickedRestaurant.getId());
        restaurant.setReviews(clickedRestaurant.getReviews());
        restaurant.setLastModificationDate(clickedRestaurant.getLastModificationDate());

        for (String image : getImagesFromListView()) {
            File file = new File(image);
            if (hasToBeInserted(file)) {
                Dialoger.showAlertDialog(image + " da inserire"); // dbg
                daoFactory = DAOFactory.getInstance();
                imageDAO = daoFactory.getImageDAO(ConfigFileReader.getProperty("image_storage_technology"));
                String imageHostUrl = null;
                try {
                    imageHostUrl = imageDAO.load(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    if (!restaurantDAO.updateImage(restaurant, imageHostUrl) || imageHostUrl == null)
                        Dialoger.showAlertDialog("Qualcosa è andato storto");
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            if (!restaurantDAO.update(restaurant))
                Dialoger.showAlertDialog("Qualcosa è andato storto durante l'update di restaurant");
            else
                updateImages(restaurant, imagesSelectedToDelete);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        if (hasChangedCity(clickedRestaurant, restaurant)) {
            daoFactory = DAOFactory.getInstance();
            CityDAO cityDAO = daoFactory.getCityDAO(ConfigFileReader.getProperty("city_storage_technology"));
            try {
                if (!cityDAO.delete(clickedRestaurant))
                    Dialoger.showAlertDialog("Non è stato possibile eliminare"); // dbg
                if (!cityDAO.insert(restaurant))
                    Dialoger.showAlertDialog("Non è stato possibile inserire"); // dbg
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        setViewsAsDefault(crudRestaurantView);
    }

    private void updateImages(Restaurant restaurant, ObservableList<String> images) throws IOException, InterruptedException {
        daoFactory = DAOFactory.getInstance();
        imageDAO = daoFactory.getImageDAO(ConfigFileReader.getProperty("image_storage_technology"));
        restaurantDAO = daoFactory.getRestaurantDAO(ConfigFileReader.getProperty("restaurant_storage_technology"));
        if (images != null) {
            for (String imageUrl : images) {
                if (!imageDAO.delete(imageUrl) || !restaurantDAO.deleteImage(restaurant, imageUrl))
                    Dialoger.showAlertDialog("Modifica non avvenuta");
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

    private void loadRestaurantsIntoTableView(int page, int size) {
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

    private void setProperOpeningHourIntoCheckBox(Restaurant restaurant) {
        crudRestaurantView.getTextFieldOpeningTime().setText(restaurant.getOpeningTime());
    }

    private void clearTypeOfCuisineCheckBox() {
        for (TypeOfCuisineItem typeOfCuisineItem : crudRestaurantView.getTableViewTypeOfCuisine().getItems())
            typeOfCuisineItem.getCheckBox().setSelected(false);
    }

    @Override
    protected Accomodation getAccomodationByFormData(CrudView crudView) {
        Restaurant restaurant = new Restaurant(super.getAccomodationByFormData(crudRestaurantView));
        restaurant.setTypeOfCuisine(getTypeOfCuisineByFormData());
        restaurant.setOpeningTime(getOpeningTimeByFormData());
        return restaurant;
    }

    private List<String> getTypeOfCuisineByFormData() {
        List<String> cuisineSelected = new ArrayList<>();
        for (TypeOfCuisineItem typeOfCuisineItem : crudRestaurantView.getTableViewTypeOfCuisine().getItems()) {
            if (typeOfCuisineItem.getCheckBox().isSelected())
                cuisineSelected.add(typeOfCuisineItem.getName());
        }
        return cuisineSelected;
    }

    public String getTipoIndirizzo() {
        return this.crudRestaurantView.getChoiceBoxIndirizzo().getValue();
    }

    private String getOpeningTimeByFormData() {
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
}