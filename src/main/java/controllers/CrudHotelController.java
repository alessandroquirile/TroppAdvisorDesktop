package controllers;

import controllers_utils.Dialoger;
import controllers_utils.InputValidator;
import dao_interfaces.CityDAO;
import dao_interfaces.HotelDAO;
import factories.DAOFactory;
import factories.FormCheckerFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.Accomodation;
import models.Hotel;
import utils.ConfigFileReader;
import views.CrudHotelView;
import views.CrudView;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public class CrudHotelController extends CrudController {
    private final CrudHotelView crudHotelView;
    private HotelDAO hotelDAO;

    public CrudHotelController(CrudHotelView crudHotelView) {
        this.crudHotelView = crudHotelView;
    }

    @Override
    public Stage getStage() {
        return (Stage) this.getCrudHotelView().getRootPane().getScene().getWindow();
    }

    @Override
    public void setViewsAsDefault(CrudView crudView) {
        super.setViewsAsDefault(crudHotelView);
        crudHotelView.getChoiceBoxIndirizzo().getSelectionModel().clearSelection();
        crudHotelView.getChoiceBoxNumeroStelle().getSelectionModel().clearSelection();
        crudHotelView.getChoiceBoxNumeroStelle().setDisable(true);
        loadHotelsIntoTableView(currentPage, currentPageSize);
    }

    @Override
    protected void enableAllChoiceBoxes(CrudView crudView) {
        super.enableAllChoiceBoxes(crudView);
        crudHotelView.getChoiceBoxNumeroStelle().setDisable(false);
    }

    @Override
    public void buttonEliminaClicked() {
        crudHotelView.getButtonElimina().setOnAction(event -> {
            daoFactory = DAOFactory.getInstance();
            hotelDAO = daoFactory.getHotelDAO(ConfigFileReader.getProperty("hotel_storage_technology"));
            Hotel selectedHotel = (Hotel) getSelectedAccomodationFromTableView(crudHotelView);
            if (selectedHotel != null) {
                if (Dialoger.areYouSureToDelete(selectedHotel.getName())) {
                    try {
                        if (!hotelDAO.delete(selectedHotel))
                            Dialoger.showAlertDialog("Qualcosa è andato storto durante la cancellazione");
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            setViewsAsDefault(crudHotelView);
        });
    }

    @Override
    protected void initializeBoxes(CrudView crudView) {
        super.initializeBoxes(crudView);
        initializeChoiceBoxNumeroStelle();
    }

    @Override
    public void tableViewClicked() {
        crudHotelView.getTableView().setOnMouseClicked(event -> {
            Hotel selectedHotel = (Hotel) getSelectedAccomodationFromTableView(crudHotelView);
            if (selectedHotel != null) {
                crudHotelView.getButtonAnnulla().setDisable(false);
                crudHotelView.getButtonElimina().setDisable(false);
                crudHotelView.getButtonModifica().setDisable(false);
                crudHotelView.getListViewFotoPath().getItems().clear();
                populateTextFieldsWithSelectedAccomodationData(selectedHotel, crudHotelView);
            }
        });
    }

    @Override
    protected void populateTextFieldsWithSelectedAccomodationData(Accomodation accomodation, CrudView crudView) {
        super.populateTextFieldsWithSelectedAccomodationData(accomodation, crudView);
        setProperStarsIntoStarsChoiceBox((Hotel) accomodation);
    }

    @Override
    public void buttonMostraAvantiClicked() {
        crudHotelView.getButtonMostraAvanti().setOnAction(event -> {
            if (!crudHotelView.getTableView().getItems().isEmpty())
                loadHotelsIntoTableView(++currentPage, currentPageSize);
        });
    }

    @Override
    public void buttonMostraIndietroClicked() {
        crudHotelView.getButtonMostraIndietro().setOnAction(event -> {
            if (currentPage != 0)
                loadHotelsIntoTableView(--currentPage, currentPageSize);
        });
    }

    @Override
    protected void buttonCercaClickedEvent(CrudView crudView) {
        super.buttonCercaClickedEvent(crudView);
        crudHotelView.getChoiceBoxNumeroStelle().getSelectionModel().clearSelection();
    }

    @Override
    public void buttonConfermaClicked() {
        crudHotelView.getButtonConferma().setOnAction(event -> {
            String telephoneNumber = getNumeroDiTelefono();
            String prezzoMedio = getPrezzoMedio();
            String cap = getCAP();

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
                                Hotel hotel = (Hotel) getAccomodationByFormData(crudHotelView);
                                daoFactory = DAOFactory.getInstance();
                                hotelDAO = daoFactory.getHotelDAO(ConfigFileReader.getProperty("hotel_storage_technology"));
                                if (modifying) {
                                    doUpdate(hotel);
                                    imagesSelectedToDelete = null;
                                } else
                                    doInsert(hotel);
                            } else
                                Dialoger.showAlertDialog("CAP non valido");
                        } else
                            Dialoger.showAlertDialog("Prezzo medio non valido. Inserire un intero");
                    } else
                        Dialoger.showAlertDialog("Numero di telefono non valido");
                }
            }
        });
    }

    private void doRetrieveByQuery() throws IOException, InterruptedException {
        String query = getQuery(crudHotelView);

        daoFactory = DAOFactory.getInstance();
        hotelDAO = daoFactory.getHotelDAO(ConfigFileReader.getProperty("hotel_storage_technology"));

        List<Hotel> hotels = (query.equals("")) ? hotelDAO.retrieveAt(currentPage, currentPageSize)
                : hotelDAO.retrieveAt(query, currentPage, currentPageSize);

        if (hotels != null) {
            final ObservableList<Object> data = FXCollections.observableArrayList(hotels);
            fillColumnsWithData();
            crudHotelView.getTableView().setItems(data);
            crudHotelView.getTableView().setDisable(false);
        } else {
            Dialoger.showAlertDialog("Non sono stati trovati hotel con questi criteri: " + query +
                    "&page=" + currentPage + "&size=" + currentPageSize);
        }
        disableCRUDButtons(crudHotelView);
    }

    @Override
    public String getQuery(CrudView crudView) {
        String query = super.getQuery(crudView);
        if (!query.isEmpty()) {
            final Integer stars = getStars();
            if (stars != null) {
                query += ";stars==\"" + stars + "\"";
            }
        }
        return query;
    }

    private void doInsert(Hotel hotel) {
        try {
            if (!hotelDAO.add(hotel))
                Dialoger.showAlertDialog("Qualcosa è andato storto durante l'inserimento");
            else
                Dialoger.showAlertDialog("Inserimento avvenuto");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        setViewsAsDefault(crudHotelView);
    }

    private void doUpdate(Hotel hotel) {
        Hotel clickedHotel = (Hotel) crudHotelView.getTableView().getSelectionModel().getSelectedItem();
        hotel.setId(clickedHotel.getId());
        hotel.setReviews(clickedHotel.getReviews());
        hotel.setLastModificationDate(clickedHotel.getLastModificationDate());

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
                    if (!hotelDAO.updateImage(hotel, imageHostUrl) || imageHostUrl == null)
                        Dialoger.showAlertDialog("Qualcosa è andato storto");
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            if (!hotelDAO.update(hotel))
                Dialoger.showAlertDialog("Qualcosa è andato storto durante l'update di hotel");
            else
                updateImages(hotel, imagesSelectedToDelete);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        if (hasChangedCity(clickedHotel, hotel)) {
            daoFactory = DAOFactory.getInstance();
            CityDAO cityDAO = daoFactory.getCityDAO(ConfigFileReader.getProperty("city_storage_technology"));
            try {
                if (!cityDAO.delete(clickedHotel))
                    Dialoger.showAlertDialog("Non è stato possibile eliminare"); // dbg
                if (!cityDAO.insert(hotel))
                    Dialoger.showAlertDialog("Non è stato possibile inserire"); // dbg
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        setViewsAsDefault(crudHotelView);
    }

    private void setProperStarsIntoStarsChoiceBox(Hotel hotel) {
        if (hotel.getStars() == 1)
            crudHotelView.getChoiceBoxNumeroStelle().getSelectionModel().select(0);
        if (hotel.getStars() == 2)
            crudHotelView.getChoiceBoxNumeroStelle().getSelectionModel().select(1);
        if (hotel.getStars() == 3)
            crudHotelView.getChoiceBoxNumeroStelle().getSelectionModel().select(2);
        if (hotel.getStars() == 4)
            crudHotelView.getChoiceBoxNumeroStelle().getSelectionModel().select(3);
        if (hotel.getStars() == 5)
            crudHotelView.getChoiceBoxNumeroStelle().getSelectionModel().select(4);
    }

    private void initializeChoiceBoxNumeroStelle() {
        ObservableList<Integer> observableList = FXCollections.observableArrayList(
                Integer.parseInt("1"),
                Integer.parseInt("2"),
                Integer.parseInt("3"),
                Integer.parseInt("4"),
                Integer.parseInt("5"));
        crudHotelView.getChoiceBoxNumeroStelle().setItems(observableList);
        crudHotelView.getChoiceBoxIndirizzo().getSelectionModel().selectFirst();
    }

    private void loadHotelsIntoTableView(int page, int size) {
        daoFactory = DAOFactory.getInstance();
        hotelDAO = daoFactory.getHotelDAO(ConfigFileReader.getProperty("hotel_storage_technology"));
        try {
            List<Hotel> hotels = hotelDAO.retrieveAt(page, size);
            if (hotels != null) {
                final ObservableList<Object> data = FXCollections.observableArrayList(hotels);
                fillColumnsWithData();
                crudHotelView.getTableView().setItems(data);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void fillColumnsWithData() {
        crudHotelView.getTableColumnId().setCellValueFactory(new PropertyValueFactory<>("id"));
        crudHotelView.getTableColumnName().setCellValueFactory(new PropertyValueFactory<>("name"));
        crudHotelView.getTableColumnNumeroStelle().setCellValueFactory(new PropertyValueFactory<>("stars"));
        crudHotelView.getTableColumnVotoMedio().setCellValueFactory(new PropertyValueFactory<>("avarageRating"));
        crudHotelView.getTableColumnPrezzoMedio().setCellValueFactory(new PropertyValueFactory<>("avaragePrice"));
        crudHotelView.getTableColumnNumeroDiTelefono().setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        crudHotelView.getTableColumnTotalReview().setCellValueFactory(new PropertyValueFactory<>("totalReviews"));
        crudHotelView.getTableColumnHasCertificateOfExcellence().setCellValueFactory(new PropertyValueFactory<>("hasCertificateOfExcellence"));
        crudHotelView.getTableColumnAddedDate().setCellValueFactory(new PropertyValueFactory<>("addedDate"));
        crudHotelView.getTableColumnLastModificationDate().setCellValueFactory(new PropertyValueFactory<>("lastModificationDate"));
        crudHotelView.getTableColumnIndirizzo().setCellValueFactory(new PropertyValueFactory<>("address"));
        crudHotelView.getTableColumnPuntoSuMappa().setCellValueFactory(new PropertyValueFactory<>("point"));
        crudHotelView.getTableColumnImmagini().setCellValueFactory(new PropertyValueFactory<>("images"));
    }

    private void updateImages(Hotel hotel, ObservableList<String> images) throws IOException, InterruptedException {
        daoFactory = DAOFactory.getInstance();
        imageDAO = daoFactory.getImageDAO(ConfigFileReader.getProperty("image_storage_technology"));
        hotelDAO = daoFactory.getHotelDAO(ConfigFileReader.getProperty("hotel_storage_technology"));
        if (images != null) {
            for (String imageUrl : images) {
                if (!imageDAO.delete(imageUrl) || !hotelDAO.deleteImage(hotel, imageUrl))
                    Dialoger.showAlertDialog("Modifica non avvenuta");
            }
            //CrudDialoger.showAlertDialog(this, "Modifica effettuata");
        }
    }

    /*@Override
    protected Accomodation getAccomodationByFormData(CrudView crudView) {
        Hotel hotel = (Hotel) super.getAccomodationByFormData(crudView);
        hotel.setStars(crudHotelView.getChoiceBoxNumeroStelle().getValue());
        return hotel;
    }*/

    @Override
    protected Accomodation getAccomodationByFormData(CrudView crudView) {
        Hotel hotel = new Hotel(super.getAccomodationByFormData(crudHotelView));
        hotel.setStars(crudHotelView.getChoiceBoxNumeroStelle().getValue());
        return hotel;
    }

    /*private Hotel getHotelByFormData() {
        Hotel hotel = new Hotel();
        hotel.setName(crudHotelView.getTextFieldNome().getText());
        hotel.setStars(crudHotelView.getChoiceBoxNumeroStelle().getValue());
        hotel.setAddress(getAddressByFormData(crudHotelView));
        hotel.setAvaragePrice(Integer.parseInt(crudHotelView.getTextFieldPrezzoMedio().getText()));
        hotel.setPhoneNumber(crudHotelView.getTextFieldNumeroDiTelefono().getText());
        hotel.setHasCertificateOfExcellence(crudHotelView.getCheckBoxCertificatoDiEccellenza().isSelected());
        hotel.setImages(crudHotelView.getListViewFotoPath().getItems());
        geocoderFactory = GeocoderFactory.getInstance();
        geocoder = geocoderFactory.getGeocoder(ConfigFileReader.getProperty("geocoder_technology"));
        hotel.setPoint(new Point(geocoder.forward(getEligibleStringAddressForGeocoding(crudHotelView)).getLatitude(),
                geocoder.forward(getEligibleStringAddressForGeocoding(crudHotelView)).getLongitude()));
        hotel.setAddedDate(getCurrentDate());
        return hotel;
    }*/

    public CrudHotelView getCrudHotelView() {
        return crudHotelView;
    }

    public String getNome() {
        return this.crudHotelView.getTextFieldNome().getText();
    }

    public String getStrada() {
        return this.crudHotelView.getTextFieldStrada().getText();
    }

    public String getNumeroCivico() {
        return this.crudHotelView.getTxtFieldNumeroCivico().getText();
    }

    public String getTipoIndirizzo() {
        return this.crudHotelView.getChoiceBoxIndirizzo().getValue();
    }

    public String getProvincia() {
        return this.crudHotelView.getTextFieldProvincia().getText();
    }

    public Integer getStars() {
        return this.crudHotelView.getChoiceBoxNumeroStelle().getValue();
    }

    public String getCAP() {
        return this.crudHotelView.getTextFieldCAP().getText();
    }

    public String getCity() {
        return this.crudHotelView.getTextFieldCity().getText();
    }

    public String getPrezzoMedio() {
        return this.crudHotelView.getTextFieldPrezzoMedio().getText();
    }

    public String getNumeroDiTelefono() {
        return this.crudHotelView.getTextFieldNumeroDiTelefono().getText();
    }

    public ObservableList<String> getImagesFromListView() {
        return this.crudHotelView.getListViewFotoPath().getItems();
    }
}
