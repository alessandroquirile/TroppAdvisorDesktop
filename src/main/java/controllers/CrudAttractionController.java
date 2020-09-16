package controllers;

import controllers_utils.CrudDialoger;
import controllers_utils.InputValidator;
import dao_interfaces.AttractionDAO;
import dao_interfaces.CityDAO;
import factories.DAOFactory;
import factories.FormCheckerFactory;
import geocoding.Geocoder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Address;
import models.Attraction;
import models_helpers.Point;
import utils.ConfigFileReader;
import views.CrudAttractionView;

import java.io.File;
import java.io.IOException;
import java.util.Date;
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
    public void setListenerOnTableView(TableView<Object> tableView) {
        if (tableView.getId().equals("tableView")) {
            tableViewClicked();
        }
    }

    @Override
    public void setViewsAsDefault() {
        crudAttractionView.getButtonIndietro().setDisable(false);
        crudAttractionView.getButtonInserisci().setDisable(false);
        crudAttractionView.getButtonModifica().setDisable(true);
        crudAttractionView.getTextFieldNumeroDiTelefono().setDisable(true);
        crudAttractionView.getTextFieldStrada().setDisable(true);
        crudAttractionView.getTxtFieldNumeroCivico().setDisable(true);
        crudAttractionView.getTextFieldProvincia().setDisable(true);
        crudAttractionView.getButtonElimina().setDisable(true);
        crudAttractionView.getButtonConferma().setDisable(true);
        crudAttractionView.getButtonAnnulla().setDisable(true);
        crudAttractionView.getButtonAiuto().setDisable(false);
        crudAttractionView.getTextFieldNome().setDisable(true);
        crudAttractionView.getTextFieldPrezzoMedio().setDisable(true);
        crudAttractionView.getChoiceBoxIndirizzo().setDisable(true);
        crudAttractionView.getTextFieldCAP().setDisable(true);
        crudAttractionView.getTextFieldCity().setDisable(true);
        crudAttractionView.getCheckBoxCertificatoDiEccellenza().setDisable(true);
        crudAttractionView.getTextFieldOpeningTime().setDisable(true);
        crudAttractionView.getChoiceBoxIndirizzo().getSelectionModel().clearSelection();
        crudAttractionView.getButtonCaricaFoto().setDisable(true);
        crudAttractionView.getListViewFotoPath().setDisable(true);
        crudAttractionView.getListViewFotoPath().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        crudAttractionView.getListViewFotoPath().getItems().clear();
        crudAttractionView.getButtonEliminaFotoSelezionate().setDisable(true);
        crudAttractionView.getTableView().setDisable(false);
        crudAttractionView.getButtonCerca().setDisable(false);
        crudAttractionView.getCheckBoxCertificatoDiEccellenza().setSelected(false);
        initializeBoxes();
        clearTextFields();
        loadAttractionsIntoTableView(currentPage, currentPageSize);
    }

    @Override
    public void setListenerOnListView(ListView<String> listViewFotoPath) {
        if (listViewFotoPath.getId().equals("listViewFotoPath")) {
            listViewFotoPathClicked();
        }
    }

    @Override
    public Stage getStage() {
        return (Stage) this.getCrudAttractionView().getRootPane().getScene().getWindow();
    }

    public void buttonCercaClicked() {
        crudAttractionView.getButtonCerca().setOnAction(event -> {
            retrieving = true;
            enableAllTextFields();
            enableAllChoiceBoxes();
            disableCRUDButtons();
            clearTextFields();
            crudAttractionView.getTableView().setDisable(true);
            crudAttractionView.getButtonConferma().setDisable(false);
            crudAttractionView.getButtonAnnulla().setDisable(false);
            crudAttractionView.getButtonIndietro().setDisable(true);
            crudAttractionView.getButtonCaricaFoto().setDisable(false);
            crudAttractionView.getListViewFotoPath().setDisable(false);
            crudAttractionView.getButtonCaricaFoto().setDisable(true);
            crudAttractionView.getListViewFotoPath().setDisable(true);
        });
    }

    public void listViewFotoPathClicked() {
        crudAttractionView.getListViewFotoPath().setOnMouseClicked(event -> {
            if (!crudAttractionView.getListViewFotoPath().getSelectionModel().getSelectedItems().isEmpty())
                crudAttractionView.getButtonEliminaFotoSelezionate().setDisable(false);
        });
    }

    public void buttonIndietroClicked() {
        crudAttractionView.getButtonIndietro().setOnAction(event -> showSelectTypeStage());
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
        crudAttractionView.getButtonInserisci().setOnAction(event -> {
            enableAllTextFields();
            enableAllChoiceBoxes();
            disableCRUDButtons();
            crudAttractionView.getTableView().setDisable(true);
            crudAttractionView.getButtonConferma().setDisable(false);
            crudAttractionView.getButtonAnnulla().setDisable(false);
            crudAttractionView.getButtonIndietro().setDisable(true);
            crudAttractionView.getButtonCaricaFoto().setDisable(false);
            crudAttractionView.getTextFieldNumeroDiTelefono().setDisable(false);
            crudAttractionView.getListViewFotoPath().setDisable(false);
        });
    }

    private void enableAllTextFields() {
        crudAttractionView.getTextFieldOpeningTime().setDisable(false);
        crudAttractionView.getTextFieldCAP().setDisable(false);
        crudAttractionView.getTextFieldCity().setDisable(false);
        crudAttractionView.getTextFieldNome().setDisable(false);
        crudAttractionView.getTextFieldNumeroDiTelefono().setDisable(false);
        crudAttractionView.getTextFieldPrezzoMedio().setDisable(false);
        crudAttractionView.getTextFieldStrada().setDisable(false);
        crudAttractionView.getTxtFieldNumeroCivico().setDisable(false);
        crudAttractionView.getTextFieldProvincia().setDisable(false);
        crudAttractionView.getTextFieldNumeroDiTelefono().setDisable(false);
    }

    private void enableAllChoiceBoxes() {
        crudAttractionView.getCheckBoxCertificatoDiEccellenza().setDisable(false);
        crudAttractionView.getChoiceBoxIndirizzo().setDisable(false);
    }

    private void disableCRUDButtons() {
        crudAttractionView.getButtonCerca().setDisable(true);
        crudAttractionView.getButtonInserisci().setDisable(true);
        crudAttractionView.getButtonModifica().setDisable(true);
        crudAttractionView.getButtonElimina().setDisable(true);
    }

    public void buttonModificaClicked() {
        crudAttractionView.getButtonModifica().setOnAction(event -> {
            modifying = true;
            retrieving = false;
            crudAttractionView.getButtonInserisci().setDisable(true);
            crudAttractionView.getButtonConferma().setDisable(false);
            crudAttractionView.getButtonAnnulla().setDisable(false);
            crudAttractionView.getButtonCerca().setDisable(true);
            crudAttractionView.getButtonModifica().setDisable(true);
            enableAllTextFields();
            enableAllChoiceBoxes();
            crudAttractionView.getButtonCaricaFoto().setDisable(false);
            crudAttractionView.getListViewFotoPath().setDisable(false);
        });
    }

    public void buttonEliminaClicked() {
        crudAttractionView.getButtonElimina().setOnAction(event -> {
            daoFactory = DAOFactory.getInstance();
            attractionDAO = daoFactory.getAttractionDAO(ConfigFileReader.getProperty("attraction_storage_technology"));
            Attraction selectedAttraction = getSelectedAttractionFromTableView();
            if (selectedAttraction != null) {
                if (CrudDialoger.areYouSureToDelete(selectedAttraction.getName())) {
                    try {
                        if (!attractionDAO.delete(selectedAttraction))
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
    }

    private void clearTextFields() {
        crudAttractionView.getTextFieldNome().setText("");
        crudAttractionView.getTextFieldCAP().setText("");
        crudAttractionView.getTextFieldCity().setText("");
        crudAttractionView.getTextFieldPrezzoMedio().setText("");
        crudAttractionView.getTextFieldNumeroDiTelefono().setText("");
        crudAttractionView.getTextFieldStrada().setText("");
        crudAttractionView.getTxtFieldNumeroCivico().setText("");
        crudAttractionView.getTextFieldProvincia().setText("");
        crudAttractionView.getTextFieldOpeningTime().setText("");
    }

    private void initializeChoiceBoxIndirizzo() {
        ObservableList<String> observableList = FXCollections.observableArrayList("Via", "Viale", "Vico", "Piazza", "Largo");
        crudAttractionView.getChoiceBoxIndirizzo().setItems(observableList);
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

    public void tableViewClicked() {
        crudAttractionView.getTableView().setOnMouseClicked(event -> {
            Attraction selectedAttraction = getSelectedAttractionFromTableView();
            if (selectedAttraction != null) {
                crudAttractionView.getButtonAnnulla().setDisable(false);
                crudAttractionView.getButtonElimina().setDisable(false);
                crudAttractionView.getButtonModifica().setDisable(false);
                crudAttractionView.getListViewFotoPath().getItems().clear();
                populateTextFieldsWithSelectedAttractionData(selectedAttraction);
            }
        });
    }

    private void populateTextFieldsWithSelectedAttractionData(Attraction attraction) {
        crudAttractionView.getTextFieldNome().setText(attraction.getName());
        crudAttractionView.getTextFieldNumeroDiTelefono().setText(attraction.getPhoneNumber());
        setProperAddressTypeIntoAddressTypeChoiceBox(attraction);
        crudAttractionView.getTextFieldStrada().setText(attraction.getStreet());
        crudAttractionView.getTxtFieldNumeroCivico().setText(attraction.getHouseNumber());
        crudAttractionView.getTextFieldCity().setText(attraction.getCity());
        crudAttractionView.getTextFieldCAP().setText(attraction.getPostalCode());
        crudAttractionView.getTextFieldProvincia().setText(attraction.getProvince());
        crudAttractionView.getTextFieldPrezzoMedio().setText(String.valueOf(attraction.getAvaragePrice()));
        crudAttractionView.getCheckBoxCertificatoDiEccellenza().setSelected(attraction.isHasCertificateOfExcellence());
        setProperOpeningHourIntoCheckBox(attraction);
        setProperImagesIntoListView(attraction);
    }

    private void setProperAddressTypeIntoAddressTypeChoiceBox(Attraction attraction) {
        if (attraction.getTypeOfAddress().equals("Via"))
            crudAttractionView.getChoiceBoxIndirizzo().getSelectionModel().select(0);
        if (attraction.getTypeOfAddress().equals("Viale"))
            crudAttractionView.getChoiceBoxIndirizzo().getSelectionModel().select(1);
        if (attraction.getTypeOfAddress().equals("Vico"))
            crudAttractionView.getChoiceBoxIndirizzo().getSelectionModel().select(2);
        if (attraction.getTypeOfAddress().equals("Piazza"))
            crudAttractionView.getChoiceBoxIndirizzo().getSelectionModel().select(3);
        if (attraction.getTypeOfAddress().equals("Largo"))
            crudAttractionView.getChoiceBoxIndirizzo().getSelectionModel().select(4);
    }

    private void setProperImagesIntoListView(Attraction attraction) {
        if (attraction.getImages() != null) {
            for (String image : attraction.getImages()) {
                crudAttractionView.getListViewFotoPath().getItems().add(image);
            }
        }
    }

    private void setProperOpeningHourIntoCheckBox(Attraction attraction) {
        crudAttractionView.getTextFieldOpeningTime().setText(attraction.getOpeningTime());
    }

    public void buttonMostraAvantiClicked() {
        crudAttractionView.getButtonMostraAvanti().setOnAction(event -> {
            if (!crudAttractionView.getTableView().getItems().isEmpty())
                loadAttractionsIntoTableView(++currentPage, currentPageSize);
        });
    }

    public void buttonMostraIndietroClicked() {
        crudAttractionView.getButtonMostraIndietro().setOnAction(event -> {
            if (currentPage != 0)
                loadAttractionsIntoTableView(--currentPage, currentPageSize);
        });
    }

    public void buttonConfermaClicked() {
        crudAttractionView.getButtonConferma().setOnAction(event -> {
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
                                        Attraction attraction = getAttractionWithFormData();
                                        daoFactory = DAOFactory.getInstance();
                                        attractionDAO = daoFactory.getAttractionDAO(ConfigFileReader.getProperty("attraction_storage_technology"));
                                        if (modifying) {
                                            doUpdate(attraction);
                                            imagesSelectedToDelete = null;
                                        } else
                                            doInsert(attraction);
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
        attractionDAO = daoFactory.getAttractionDAO(ConfigFileReader.getProperty("attraction_storage_technology"));

        List<Attraction> attractions = (query.equals("")) ? attractionDAO.retrieveAt(currentPage, currentPageSize)
                : attractionDAO.retrieveByQuery(query, currentPage, currentPageSize);

        if (attractions != null) {
            String openingTimeDesired = getOpeningTimeWithFormData();
            if (!openingTimeDesired.isEmpty())
                attractions.removeIf(attraction -> !attraction.getOpeningTime().equals(openingTimeDesired));
            final ObservableList<Object> data = FXCollections.observableArrayList(attractions);
            fillColumnsWithData();
            crudAttractionView.getTableView().setItems(data);
            crudAttractionView.getTableView().setDisable(false);
        } else {
            CrudDialoger.showAlertDialog("Non sono state trovate attrazioni con questi criteri: " + query +
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
        String certificatoDiEccellenza = String.valueOf(crudAttractionView.getCheckBoxCertificatoDiEccellenza().isSelected());
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

        if (crudAttractionView.getCheckBoxCertificatoDiEccellenza().isSelected()) {
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

    private void doInsert(Attraction attraction) {
        try {
            if (!attractionDAO.add(attraction))
                CrudDialoger.showAlertDialog("Qualcosa è andato storto durante l'inserimento");
            else
                CrudDialoger.showAlertDialog("Inserimento avvenuto");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        setViewsAsDefault();
    }

    private void doUpdate(Attraction attraction) {
        Attraction clickedAttraction = (Attraction) crudAttractionView.getTableView().getSelectionModel().getSelectedItem();
        attraction.setId(clickedAttraction.getId());
        attraction.setReviews(clickedAttraction.getReviews());
        attraction.setLastModificationDate(clickedAttraction.getLastModificationDate());

        //CrudDialoger.showAlertDialog(this, "Old City: " + clickedAttraction.getCity() + "\nNew City: " + attraction.getCity()); // dbg

        //CrudDialoger.showAlertDialog(this, "imagesSelectedToDelete: " + imagesSelectedToDelete); // dbg

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
                    if (attractionDAO.updateAttractionSingleImageFromAttractionCollection(attraction, imageHostUrl) || imageHostUrl == null)
                        CrudDialoger.showAlertDialog("Qualcosa è andato storto");
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            if (!attractionDAO.update(attraction))
                CrudDialoger.showAlertDialog("Qualcosa è andato storto durante l'update di attraction");
            else
                updateImages(attraction, imagesSelectedToDelete);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        if (!clickedAttraction.getCity().equals(attraction.getCity())) {
            //CrudDialoger.showAlertDialog(this, clickedAttraction.getCity() + " not equal to " + attraction.getCity());
            daoFactory = DAOFactory.getInstance();
            CityDAO cityDAO = daoFactory.getCityDAO(ConfigFileReader.getProperty("city_storage_technology"));
            try {
                //System.out.println(clickedAttraction);
                if (!cityDAO.delete(clickedAttraction))
                    CrudDialoger.showAlertDialog("Non è stato possibile eliminare"); // dbg
                if (!cityDAO.insert(attraction))
                    CrudDialoger.showAlertDialog("Non è stato possibile inserire"); // dbg
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        setViewsAsDefault();
    }

    private void updateImages(Attraction attraction, ObservableList<String> images) throws IOException, InterruptedException {
        daoFactory = DAOFactory.getInstance();
        imageDAO = daoFactory.getImageDAO(ConfigFileReader.getProperty("image_storage_technology"));
        attractionDAO = daoFactory.getAttractionDAO(ConfigFileReader.getProperty("attraction_storage_technology"));
        if (images != null) {
            for (String imageUrl : images) {
                if (!imageDAO.deleteThisImageFromBucket(imageUrl) || !attractionDAO.deleteAttractionSingleImageFromAttractionCollection(attraction, imageUrl))
                    CrudDialoger.showAlertDialog("Modifica non avvenuta");
            }
            //CrudDialoger.showAlertDialog(this, "Modifica effettuata"); //
        }
    }

    public void buttonAiutoClicked() {
        crudAttractionView.getButtonAiuto().setOnAction(event -> CrudDialoger.showHelpDialog());
    }

    public void buttonCaricaClicked() {
        crudAttractionView.getButtonCaricaFoto().setOnAction(event -> multiFileSelectionFromFileSystem());
    }

    private void multiFileSelectionFromFileSystem() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        FileChooser.ExtensionFilter imageFilter =
                new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.jpeg");
        fileChooser.getExtensionFilters().add(imageFilter);
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(null);
        if (selectedFiles != null) {
            for (File selectedFile : selectedFiles) {
                crudAttractionView.getListViewFotoPath().getItems().add(selectedFile.getAbsolutePath());
            }
        } else {
            CrudDialoger.showAlertDialog("I file selezionati non sono validi");
        }
    }

    public void buttonEliminaFotoSelezionataClicked() {
        crudAttractionView.getButtonEliminaFotoSelezionate().setOnAction(event -> {
            ObservableList<String> imagesSelectedToDelete2 = crudAttractionView.getListViewFotoPath().getSelectionModel().getSelectedItems();
            imagesSelectedToDelete = FXCollections.observableArrayList(imagesSelectedToDelete2);
            crudAttractionView.getListViewFotoPath().getItems().removeAll(imagesSelectedToDelete2);
        });
    }

    public void buttonAnnullaClicked() {
        crudAttractionView.getButtonAnnulla().setOnAction(event -> {
            setViewsAsDefault();
            retrieving = false;
            modifying = false;
        });
    }

    private Attraction getAttractionWithFormData() {
        Attraction attraction = new Attraction();
        attraction.setName(crudAttractionView.getTextFieldNome().getText());
        attraction.setAddress(getAddressWithFormData());
        attraction.setAvaragePrice(Integer.parseInt(crudAttractionView.getTextFieldPrezzoMedio().getText()));
        attraction.setPhoneNumber(crudAttractionView.getTextFieldNumeroDiTelefono().getText());
        attraction.setHasCertificateOfExcellence(crudAttractionView.getCheckBoxCertificatoDiEccellenza().isSelected());
        attraction.setImages(crudAttractionView.getListViewFotoPath().getItems());
        attraction.setPoint(new Point(Geocoder.reverseGeocoding(getEligibleStringAddressForGeocoding()).getLat(),
                Geocoder.reverseGeocoding(getEligibleStringAddressForGeocoding()).getLng()));
        attraction.setAddedDate(getCurrentDate());
        attraction.setOpeningTime(getOpeningTimeWithFormData());
        return attraction;
    }

    private Address getAddressWithFormData() {
        return new Address(
                crudAttractionView.getChoiceBoxIndirizzo().getValue(),
                crudAttractionView.getTextFieldStrada().getText(),
                crudAttractionView.getTxtFieldNumeroCivico().getText(),
                crudAttractionView.getTextFieldCity().getText(),
                crudAttractionView.getTextFieldProvincia().getText(),
                crudAttractionView.getTextFieldCAP().getText());
    }

    private String getEligibleStringAddressForGeocoding() {
        return crudAttractionView.getChoiceBoxIndirizzo().getValue() + " " + crudAttractionView.getTextFieldStrada().getText() + ", " +
                crudAttractionView.getTxtFieldNumeroCivico().getText() + ", " + crudAttractionView.getTextFieldCity().getText() + ", " + crudAttractionView.getTextFieldCAP().getText() +
                ", " + crudAttractionView.getTextFieldProvincia().getText();
    }

    private String getCurrentDate() {
        return new Date().toString();
    }

    private String getOpeningTimeWithFormData() {
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

    public Attraction getSelectedAttractionFromTableView() {
        return (Attraction) crudAttractionView.getTableView().getSelectionModel().getSelectedItem();
    }

    public CrudAttractionView getCrudAttractionView() {
        return crudAttractionView;
    }

    public String getOpeningTime() {
        return this.crudAttractionView.getTextFieldOpeningTime().getText();
    }
}