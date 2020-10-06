package controllers;

import controllers_utils.Dialoger;
import controllers_utils.InputValidator;
import dao_interfaces.AttractionDAO;
import factories.DAOFactory;
import factories.FormCheckerFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.Accomodation;
import models.Attraction;
import utils.ConfigFileReader;
import views.CrudAttractionView;
import views.CrudView;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public class CrudAttractionController extends CrudController {
    private final CrudAttractionView crudAttractionView;
    private AttractionDAO attractionDAO;

    public CrudAttractionController(CrudAttractionView crudAttractionView) {
        this.crudAttractionView = crudAttractionView;
    }

    @Override
    public void setViewsAsDefault(CrudView crudView) {
        super.setViewsAsDefault(crudAttractionView);
        crudAttractionView.getTextFieldOpeningTime().setDisable(true);
        loadAttractionsIntoTableView(currentPage, currentPageSize);
    }

    @Override
    public Stage getStage() {
        return (Stage) this.getCrudAttractionView().getRootPane().getScene().getWindow();
    }

    @Override
    protected void enableAllTextFields(CrudView crudView) {
        super.enableAllTextFields(crudView);
        crudAttractionView.getTextFieldOpeningTime().setDisable(false);
    }

    @Override
    protected void buttonEliminaClickedEvent() {
        daoFactory = DAOFactory.getInstance();
        attractionDAO = daoFactory.getAttractionDAO(ConfigFileReader.getProperty("attraction_storage_technology"));
        Attraction selectedAttraction = (Attraction) getSelectedAccomodationFromTableView(crudAttractionView);
        if (selectedAttraction != null) {
            if (Dialoger.areYouSureToDelete(selectedAttraction.getName())) {
                try {
                    if (!attractionDAO.delete(selectedAttraction))
                        Dialoger.showAlertDialog("Qualcosa è andato storto durante la cancellazione");
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        setViewsAsDefault(crudAttractionView);
    }

    @Override
    protected void clearTextFields(CrudView crudView) {
        super.clearTextFields(crudView);
        crudAttractionView.getTextFieldOpeningTime().setText("");
    }

    @Override
    protected void tableViewClickedEvent() {
        Attraction selectedAttraction = (Attraction) getSelectedAccomodationFromTableView(crudAttractionView);
        if (selectedAttraction != null) {
            crudAttractionView.getButtonAnnulla().setDisable(false);
            crudAttractionView.getButtonElimina().setDisable(false);
            crudAttractionView.getButtonModifica().setDisable(false);
            crudAttractionView.getListViewFotoPath().getItems().clear();
            populateTextFieldsWithSelectedAccomodationData(selectedAttraction, crudAttractionView);
        }
    }

    @Override
    protected void populateTextFieldsWithSelectedAccomodationData(Accomodation accomodation, CrudView crudView) {
        super.populateTextFieldsWithSelectedAccomodationData(accomodation, crudView);
        setProperOpeningHourIntoCheckBox((Attraction) accomodation);
    }

    @Override
    protected void buttonMostraAvantiClickedEvent() {
        if (!crudAttractionView.getTableView().getItems().isEmpty())
            loadAttractionsIntoTableView(++currentPage, currentPageSize);
    }

    @Override
    protected void buttonMostraIndietroClickedEvent() {
        if (currentPage != 0)
            loadAttractionsIntoTableView(--currentPage, currentPageSize);
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
                Dialoger.showAlertDialog("Riempi i campi obbligatori");
            } else {
                if (InputValidator.isValidTelephoneNumber(telephoneNumber) || telephoneNumber.isEmpty()) {
                    if (InputValidator.isNumberGreaterOrEqualToZero(prezzoMedio)) {
                        if (InputValidator.isNumberGreaterOrEqualToZero(cap)) {
                            if (InputValidator.isValidOpeningTime(openingTime)) {
                                Attraction attraction = (Attraction) getAccomodationByFormData(crudAttractionView);
                                daoFactory = DAOFactory.getInstance();
                                attractionDAO = daoFactory.getAttractionDAO(ConfigFileReader.getProperty("attraction_storage_technology"));
                                if (modifying) {
                                    doUpdate(attraction);
                                    imagesSelectedToDelete = null;
                                } else
                                    doInsert(attraction);
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

    @Override
    protected Accomodation getAccomodationByFormData(CrudView crudView) {
        Attraction attraction = new Attraction(super.getAccomodationByFormData(crudAttractionView));
        attraction.setOpeningTime(getOpeningTimeByFormData());
        return attraction;
    }

    private void doRetrieveByQuery() throws IOException, InterruptedException {
        String query = getQuery(crudAttractionView);

        Dialoger.showAlertDialog(query); // dbg

        daoFactory = DAOFactory.getInstance();
        attractionDAO = daoFactory.getAttractionDAO(ConfigFileReader.getProperty("attraction_storage_technology"));

        List<Attraction> attractions = (query.equals("")) ? attractionDAO.retrieveAt(currentPage, currentPageSize)
                : attractionDAO.retrieveAt(query, currentPage, currentPageSize);

        if (attractions != null) {
            String openingTimeDesired = getOpeningTimeByFormData();
            if (!openingTimeDesired.isEmpty())
                attractions.removeIf(attraction -> !attraction.getOpeningTime().equals(openingTimeDesired));
            final ObservableList<Object> data = FXCollections.observableArrayList(attractions);
            fillColumnsWithData();
            crudAttractionView.getTableView().setItems(data);
            crudAttractionView.getTableView().setDisable(false);
        } else {
            Dialoger.showAlertDialog("Non sono state trovate attrazioni con questi criteri: " + query +
                    "&page=" + currentPage + "&size=" + currentPageSize);
        }
        disableCRUDButtons(crudAttractionView);
    }

    private void doInsert(Attraction attraction) {
        try {
            if (!attractionDAO.add(attraction))
                Dialoger.showAlertDialog("Qualcosa è andato storto durante l'inserimento");
            else
                Dialoger.showAlertDialog("Inserimento avvenuto");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        setViewsAsDefault(crudAttractionView);
    }

    private void doUpdate(Attraction attraction) {
        Attraction clickedAttraction = (Attraction) crudAttractionView.getTableView().getSelectionModel().getSelectedItem();
        attraction.setId(clickedAttraction.getId());
        attraction.setReviews(clickedAttraction.getReviews());
        attraction.setLastModificationDate(clickedAttraction.getLastModificationDate());

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
                    if (!attractionDAO.updateImage(attraction, imageHostUrl) || imageHostUrl == null)
                        Dialoger.showAlertDialog("Qualcosa è andato storto");
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            if (!attractionDAO.update(attraction))
                Dialoger.showAlertDialog("Qualcosa è andato storto durante l'update di attraction");
            else
                updateImages(attraction, imagesSelectedToDelete);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        if (hasChangedCity(clickedAttraction, attraction)) {
            daoFactory = DAOFactory.getInstance();
            cityDAO = daoFactory.getCityDAO(ConfigFileReader.getProperty("city_storage_technology"));
            try {
                if (!cityDAO.delete(clickedAttraction))
                    Dialoger.showAlertDialog("Non è stato possibile eliminare"); // dbg
                if (!cityDAO.insert(attraction))
                    Dialoger.showAlertDialog("Non è stato possibile inserire"); // dbg
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        setViewsAsDefault(crudAttractionView);
    }

    private void fillColumnsWithData() {
        crudAttractionView.getTableColumnId().setCellValueFactory(new PropertyValueFactory<>("id"));
        crudAttractionView.getTableColumnName().setCellValueFactory(new PropertyValueFactory<>("name"));
        crudAttractionView.getTableColumnOrarioApertura().setCellValueFactory(new PropertyValueFactory<>("openingTime"));
        crudAttractionView.getTableColumnVotoMedio().setCellValueFactory(new PropertyValueFactory<>("avarageRating"));
        crudAttractionView.getTableColumnPrezzoMedio().setCellValueFactory(new PropertyValueFactory<>("avaragePrice"));
        crudAttractionView.getTableColumnNumeroDiTelefono().setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        crudAttractionView.getTableColumnTotalReview().setCellValueFactory(new PropertyValueFactory<>("totalReviews"));
        crudAttractionView.getTableColumnHasCertificateOfExcellence().setCellValueFactory(new PropertyValueFactory<>("hasCertificateOfExcellence"));
        crudAttractionView.getTableColumnAddedDate().setCellValueFactory(new PropertyValueFactory<>("addedDate"));
        crudAttractionView.getTableColumnLastModificationDate().setCellValueFactory(new PropertyValueFactory<>("lastModificationDate"));
        crudAttractionView.getTableColumnIndirizzo().setCellValueFactory(new PropertyValueFactory<>("address"));
        crudAttractionView.getTableColumnPuntoSuMappa().setCellValueFactory(new PropertyValueFactory<>("point"));
        crudAttractionView.getTableColumnImmagini().setCellValueFactory(new PropertyValueFactory<>("images"));
    }

    private void loadAttractionsIntoTableView(int page, int size) {
        daoFactory = DAOFactory.getInstance();
        attractionDAO = daoFactory.getAttractionDAO(ConfigFileReader.getProperty("attraction_storage_technology"));
        try {
            List<Attraction> attractions = attractionDAO.retrieveAt(page, size);
            if (attractions != null) {
                final ObservableList<Object> data = FXCollections.observableArrayList(attractions);
                fillColumnsWithData();
                crudAttractionView.getTableView().setItems(data);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void setProperOpeningHourIntoCheckBox(Attraction attraction) {
        crudAttractionView.getTextFieldOpeningTime().setText(attraction.getOpeningTime());
    }

    private void updateImages(Attraction attraction, ObservableList<String> images) throws IOException, InterruptedException {
        daoFactory = DAOFactory.getInstance();
        imageDAO = daoFactory.getImageDAO(ConfigFileReader.getProperty("image_storage_technology"));
        attractionDAO = daoFactory.getAttractionDAO(ConfigFileReader.getProperty("attraction_storage_technology"));
        if (images != null) {
            for (String imageUrl : images) {
                if (!imageDAO.delete(imageUrl) || !attractionDAO.deleteImage(attraction, imageUrl))
                    Dialoger.showAlertDialog("Modifica non avvenuta");
            }
        }
    }

    private String getOpeningTimeByFormData() {
        return crudAttractionView.getTextFieldOpeningTime().getText();
    }

    public String getNome() {
        return this.crudAttractionView.getTextFieldNome().getText();
    }

    public String getStrada() {
        return this.crudAttractionView.getTextFieldStrada().getText();
    }

    public String getNumeroCivico() {
        return this.crudAttractionView.getTxtFieldNumeroCivico().getText();
    }

    public String getTipoIndirizzo() {
        return this.crudAttractionView.getChoiceBoxIndirizzo().getValue();
    }

    public String getProvincia() {
        return this.crudAttractionView.getTextFieldProvincia().getText();
    }

    public String getCAP() {
        return this.crudAttractionView.getTextFieldCAP().getText();
    }

    public String getCity() {
        return this.crudAttractionView.getTextFieldCity().getText();
    }

    public String getPrezzoMedio() {
        return this.crudAttractionView.getTextFieldPrezzoMedio().getText();
    }

    public String getNumeroDiTelefono() {
        return this.crudAttractionView.getTextFieldNumeroDiTelefono().getText();
    }

    public ObservableList<String> getImagesFromListView() {
        return this.crudAttractionView.getListViewFotoPath().getItems();
    }

    public CrudAttractionView getCrudAttractionView() {
        return crudAttractionView;
    }

    public String getOpeningTime() {
        return this.crudAttractionView.getTextFieldOpeningTime().getText();
    }
}