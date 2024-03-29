package controllers;

import controllers_utils.Dialoger;
import controllers_utils.InputValidator;
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
    protected void buttonEliminaClickedEvent() {
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
                        throw new RuntimeException(e);
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
    protected void tableViewClickedEvent() {
        Hotel selectedHotel = (Hotel) getSelectedAccomodationFromTableView(crudHotelView);
        if (selectedHotel != null) {
            crudHotelView.getButtonAnnulla().setDisable(false);
            crudHotelView.getButtonElimina().setDisable(false);
            crudHotelView.getButtonInserisci().setDisable(true);
            crudHotelView.getButtonModifica().setDisable(false);
            crudHotelView.getListViewFotoPath().getItems().clear();
            populateTextFieldsWithSelectedAccomodationData(selectedHotel, crudHotelView);
        }
    }

    @Override
    protected void populateTextFieldsWithSelectedAccomodationData(Accomodation accomodation, CrudView crudView) {
        super.populateTextFieldsWithSelectedAccomodationData(accomodation, crudView);
        setProperStarsIntoStarsChoiceBox((Hotel) accomodation);
    }

    @Override
    protected void buttonMostraAvantiClickedEvent() {
        if (!crudHotelView.getTableView().getItems().isEmpty())
            loadHotelsIntoTableView(++currentPage, currentPageSize);
    }

    @Override
    protected void buttonMostraIndietroClickedEvent() {
        if (currentPage != 0)
            loadHotelsIntoTableView(--currentPage, currentPageSize);
    }

    @Override
    protected void buttonCercaClickedEvent(CrudView crudView) {
        super.buttonCercaClickedEvent(crudView);
        crudHotelView.getChoiceBoxNumeroStelle().getSelectionModel().clearSelection();
    }

    @Override
    protected void buttonConfermaClickedEvent() {
        String telephoneNumber = getNumeroDiTelefono();
        String prezzoMedio = getPrezzoMedio();
        String cap = getCAP();

        formCheckerFactory = FormCheckerFactory.getInstance();
        formChecker = formCheckerFactory.getFormChecker(this);

        if (retrieving) {
            try {
                doRetrieveByQuery();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else {
            if (formChecker.formHasSomeEmptyField(this)) {
                Dialoger.showAlertDialog("Riempi i campi");
            } else {
                if (InputValidator.isValidTelephoneNumber(telephoneNumber)) {
                    if (InputValidator.isNumberGreaterOrEqualToZero(prezzoMedio)) {
                        if (InputValidator.isIntegerGreaterOrEqualToZero(cap)) {
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
                        Dialoger.showAlertDialog("Prezzo medio non valido");
                } else
                    Dialoger.showAlertDialog("Numero di telefono non valido");
            }
        }
    }

    @Override
    protected Accomodation getAccomodationByFormData(CrudView crudView) {
        Hotel hotel = new Hotel(super.getAccomodationByFormData(crudHotelView));
        hotel.setStars(crudHotelView.getChoiceBoxNumeroStelle().getValue());
        return hotel;
    }

    @Override
    public String getRESTQuery(CrudView crudView) {
        String query = super.getRESTQuery(crudView);
        if (!query.isEmpty()) {
            final Integer stars = getStars();
            if (stars != null) {
                query += ";stars==\"" + stars + "\"";
            }
        }
        return query;
    }

    private void doRetrieveByQuery() throws IOException, InterruptedException {
        String query = getRESTQuery(crudHotelView);

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
            Dialoger.showAlertDialog("Non sono stati trovati hotel con questi criteri");
        }
        disableCRUDButtons(crudHotelView);
    }

    private void doInsert(Hotel hotel) {
        try {
            if (!hotelDAO.add(hotel))
                Dialoger.showAlertDialog("Qualcosa è andato storto durante l'inserimento");
            else
                Dialoger.showAlertDialog("Inserimento avvenuto");
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
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
                daoFactory = DAOFactory.getInstance();
                imageDAO = daoFactory.getImageDAO(ConfigFileReader.getProperty("image_storage_technology"));
                String imageHostUrl;
                try {
                    imageHostUrl = imageDAO.load(file);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    if (!hotelDAO.updateImage(hotel, imageHostUrl) || imageHostUrl == null)
                        Dialoger.showAlertDialog("Qualcosa è andato storto");
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        try {
            if (!hotelDAO.update(hotel))
                Dialoger.showAlertDialog("Qualcosa è andato storto durante l'update di hotel");
            else
                updateImages(hotel, imagesSelectedToDelete);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (hasChangedCity(clickedHotel, hotel)) {
            daoFactory = DAOFactory.getInstance();
            cityDAO = daoFactory.getCityDAO(ConfigFileReader.getProperty("city_storage_technology"));
            try {
                if (!cityDAO.delete(clickedHotel) || !cityDAO.insert(hotel))
                    Dialoger.showAlertDialog("Qualcosa è andato storto");
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
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
            throw new RuntimeException(e);
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
        crudHotelView.getTableColumnHasCertificateOfExcellence().setCellValueFactory(new PropertyValueFactory<>("certificateOfExcellence"));
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
        }
    }

    public CrudHotelView getCrudHotelView() {
        return crudHotelView;
    }

    public String getNome() {
        return this.crudHotelView.getTextFieldNome().getText().trim();
    }

    public String getStrada() {
        return this.crudHotelView.getTextFieldStrada().getText().trim();
    }

    public String getNumeroCivico() {
        return this.crudHotelView.getTxtFieldNumeroCivico().getText().trim();
    }

    public String getTipoIndirizzo() {
        return this.crudHotelView.getChoiceBoxIndirizzo().getValue().trim();
    }

    public String getProvincia() {
        return this.crudHotelView.getTextFieldProvincia().getText().trim();
    }

    public Integer getStars() {
        return this.crudHotelView.getChoiceBoxNumeroStelle().getValue();
    }

    public String getCAP() {
        return this.crudHotelView.getTextFieldCAP().getText().trim();
    }

    public String getCity() {
        return this.crudHotelView.getTextFieldCity().getText().trim();
    }

    public String getPrezzoMedio() {
        return this.crudHotelView.getTextFieldPrezzoMedio().getText().trim();
    }

    public String getNumeroDiTelefono() {
        return this.crudHotelView.getTextFieldNumeroDiTelefono().getText().trim();
    }

    public ObservableList<String> getImagesFromListView() {
        return this.crudHotelView.getListViewFotoPath().getItems();
    }
}
