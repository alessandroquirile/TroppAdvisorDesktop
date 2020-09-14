package controllers;

import controllers_utils.CrudDialoger;
import controllers_utils.InputValidator;
import dao_interfaces.AttractionDAO;
import dao_interfaces.CityDAO;
import dao_interfaces.ImageDAO;
import factories.DAOFactory;
import factories.FormCheckerFactory;
import form_checker_interfaces.FormChecker;
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
    private final int currentPageSize = 100;
    private DAOFactory daoFactory;
    private AttractionDAO attractionDAO;
    private ImageDAO imageDAO;
    private int currentPage = 0;
    private ObservableList<String> imagesSelectedToDelete;
    private FormCheckerFactory formCheckerFactory;
    private FormChecker formChecker;
    private boolean retrieving = false;

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
        crudAttractionView.getTextFieldCittà().setDisable(true);
        crudAttractionView.getCheckBoxCertificatoDiEccellenza().setDisable(true);
        crudAttractionView.getComboBoxOrarioAperturaMattutina().setDisable(true);
        crudAttractionView.getComboBoxOrarioChiusuraMattutina().setDisable(true);
        crudAttractionView.getComboBoxOrarioAperturaSerale().setDisable(true);
        crudAttractionView.getComboBoxOrarioChiusuraSerale().setDisable(true);
        crudAttractionView.getComboBoxOrarioAperturaMattutina().getSelectionModel().clearSelection();
        crudAttractionView.getComboBoxOrarioChiusuraMattutina().getSelectionModel().clearSelection();
        crudAttractionView.getComboBoxOrarioAperturaSerale().getSelectionModel().clearSelection();
        crudAttractionView.getComboBoxOrarioChiusuraSerale().getSelectionModel().clearSelection();
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

    private void buttonCercaClicked() {
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
            crudAttractionView.getTextFieldNumeroDiTelefono().setDisable(false);
            crudAttractionView.getListViewFotoPath().setDisable(false);
            crudAttractionView.getComboBoxOrarioAperturaMattutina().setDisable(true);
            crudAttractionView.getComboBoxOrarioChiusuraMattutina().setDisable(true);
            crudAttractionView.getComboBoxOrarioAperturaSerale().setDisable(true);
            crudAttractionView.getComboBoxOrarioChiusuraSerale().setDisable(true);
            crudAttractionView.getButtonCaricaFoto().setDisable(true);
            crudAttractionView.getListViewFotoPath().setDisable(true);
        });
    }

    private void listViewFotoPathClicked() {
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
        Stage thisStage = getStage();
        thisStage.close();
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
            crudAttractionView.getComboBoxOrarioAperturaMattutina().setDisable(false);
            crudAttractionView.getComboBoxOrarioChiusuraMattutina().setDisable(false);
            crudAttractionView.getComboBoxOrarioAperturaSerale().setDisable(false);
            crudAttractionView.getComboBoxOrarioChiusuraSerale().setDisable(false);
        });
    }

    public void enableAllTextFields() {
        crudAttractionView.getTextFieldCAP().setDisable(false);
        crudAttractionView.getTextFieldCittà().setDisable(false);
        crudAttractionView.getTextFieldNome().setDisable(false);
        crudAttractionView.getTextFieldNumeroDiTelefono().setDisable(false);
        crudAttractionView.getTextFieldPrezzoMedio().setDisable(false);
        crudAttractionView.getTextFieldStrada().setDisable(false);
        crudAttractionView.getTxtFieldNumeroCivico().setDisable(false);
        crudAttractionView.getTextFieldProvincia().setDisable(false);
    }

    public void enableAllChoiceBoxes() {
        crudAttractionView.getCheckBoxCertificatoDiEccellenza().setDisable(false);
        crudAttractionView.getChoiceBoxIndirizzo().setDisable(false);
    }

    public void disableCRUDButtons() {
        crudAttractionView.getButtonCerca().setDisable(true);
        crudAttractionView.getButtonInserisci().setDisable(true);
        crudAttractionView.getButtonModifica().setDisable(true);
        crudAttractionView.getButtonElimina().setDisable(true);
    }

    public void buttonModificaClicked() {
        crudAttractionView.getButtonModifica().setOnAction(event -> {
            crudAttractionView.getButtonInserisci().setDisable(true);
            crudAttractionView.getButtonConferma().setDisable(false);
            crudAttractionView.getButtonAnnulla().setDisable(false);
            enableAllTextFields();
            enableAllChoiceBoxes();
            crudAttractionView.getComboBoxOrarioAperturaMattutina().setDisable(false);
            crudAttractionView.getComboBoxOrarioChiusuraMattutina().setDisable(false);
            crudAttractionView.getComboBoxOrarioAperturaSerale().setDisable(false);
            crudAttractionView.getComboBoxOrarioChiusuraSerale().setDisable(false);
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
        initializeComboBoxOrariMattutini();
        initializeComboBoxOrariSerali();
    }

    private void clearTextFields() {
        crudAttractionView.getTextFieldNome().setText("");
        crudAttractionView.getTextFieldCAP().setText("");
        crudAttractionView.getTextFieldCittà().setText("");
        crudAttractionView.getTextFieldPrezzoMedio().setText("");
        crudAttractionView.getTextFieldNumeroDiTelefono().setText("");
        crudAttractionView.getTextFieldStrada().setText("");
        crudAttractionView.getTxtFieldNumeroCivico().setText("");
        crudAttractionView.getTextFieldProvincia().setText("");
    }

    private void initializeChoiceBoxIndirizzo() {
        ObservableList<String> observableList = FXCollections.observableArrayList("Via", "Viale", "Vico", "Piazza", "Largo");
        crudAttractionView.getChoiceBoxIndirizzo().setItems(observableList);
        //crudAttractionView.getChoiceBoxIndirizzo().getSelectionModel().selectFirst();
    }

    private void initializeComboBoxOrariMattutini() {
        ObservableList<String> orariMattutini = FXCollections.observableArrayList(
                "07:00", "07:30", "08:00", "08:30", "09:00", "09:30", "10:00", "10:30", "11:00",
                "11:30", "12:00", "12:30", "13:00", "13.30", "14:00", "14:30");
        crudAttractionView.getComboBoxOrarioAperturaMattutina().setItems(orariMattutini);
        crudAttractionView.getComboBoxOrarioChiusuraMattutina().setItems(orariMattutini);
    }

    private void initializeComboBoxOrariSerali() {
        ObservableList<String> orariSerali = FXCollections.observableArrayList(
                "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00",
                "19:30", "20:00", "20:30", "21:00", "21:30", "22:00", "22:30",
                "23:00", "23:30", "00:00", "00:30", "01:00", "01:30", "02:00", "02:30", "03:00");
        crudAttractionView.getComboBoxOrarioAperturaSerale().setItems(orariSerali);
        crudAttractionView.getComboBoxOrarioChiusuraSerale().setItems(orariSerali);
    }

    public void loadAttractionsIntoTableView(int page, int size) {
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

    private void tableViewClicked() {
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
        crudAttractionView.getTextFieldCittà().setText(attraction.getCity());
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
        String aperturaMattutina = attraction.getOpeningTime().substring(0, 5);
        String chiusuraMattutina = attraction.getOpeningTime().substring(8, 13);
        String aperturaSerale = attraction.getOpeningTime().substring(14, 19);
        String chiusuraSerale = attraction.getOpeningTime().substring(22, 27);
        crudAttractionView.getComboBoxOrarioAperturaMattutina().getSelectionModel().select(aperturaMattutina);
        crudAttractionView.getComboBoxOrarioChiusuraMattutina().getSelectionModel().select(chiusuraMattutina);
        crudAttractionView.getComboBoxOrarioAperturaSerale().getSelectionModel().select(aperturaSerale);
        crudAttractionView.getComboBoxOrarioChiusuraSerale().getSelectionModel().select(chiusuraSerale);
    }

    private void buttonMostraAvantiClicked() {
        crudAttractionView.getButtonMostraAvanti().setOnAction(event -> {
            System.out.println("Premuto Mostra avanti");
            if (!crudAttractionView.getTableView().getItems().isEmpty()) {
                currentPage++;
                loadAttractionsIntoTableView(currentPage, currentPageSize);
            }
        });
    }

    private void buttonMostraIndietroClicked() {
        crudAttractionView.getButtonMostraIndietro().setOnAction(event -> {
            System.out.println("Premuto mostra indietro");
            if (currentPage != 0) {
                currentPage--;
                loadAttractionsIntoTableView(currentPage, currentPageSize);
            }
        });
    }

    private void buttonConfermaClicked() {
        crudAttractionView.getButtonConferma().setOnAction(event -> {
            String telephoneNumber = getNumeroDiTelefono();
            String prezzoMedio = getPrezzoMedio();
            String numeroCivico = getNumeroCivico();
            String cap = getCAP();
            String orarioAperturaMattutina = getOrarioAperturaMattutina();
            String orarioChiusuraMattutina = getOrarioChiusuraMattutina();
            String orarioAperturaSerale = getOrarioAperturaSerale();
            String orarioChiusuraSerale = getOrarioChiusuraSerale();

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
                                    if (InputValidator.isValidOpeningTimeAtMorning(orarioAperturaMattutina, orarioChiusuraMattutina)) {
                                        if (InputValidator.isValidOpeningTimeAtEvening(orarioAperturaSerale, orarioChiusuraSerale)) {
                                            Attraction attraction = getAttractionWithFormData();
                                            daoFactory = DAOFactory.getInstance();
                                            attractionDAO = daoFactory.getAttractionDAO(ConfigFileReader.getProperty("attraction_storage_technology"));
                                            if (crudAttractionView.getButtonModifica().isDisable()) {
                                                doInsert(attraction);
                                            } else {
                                                doUpdate(attraction);
                                                imagesSelectedToDelete = null;
                                            }
                                        } else {
                                            CrudDialoger.showAlertDialog("Orario serale non valido");
                                        }
                                    } else {
                                        CrudDialoger.showAlertDialog("Orario mattutino non valido");
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
        String query = "";

        query = getQuery(query);

        CrudDialoger.showAlertDialog(query); // dbg

        daoFactory = DAOFactory.getInstance();
        attractionDAO = daoFactory.getAttractionDAO(ConfigFileReader.getProperty("attraction_storage_technology"));

        List<Attraction> attractions = (query.equals("")) ? attractionDAO.retrieveAt(currentPage, currentPageSize)
                : attractionDAO.retrieveByQuery(query, currentPage, currentPageSize);

        if (attractions != null) {
            final ObservableList<Object> data = FXCollections.observableArrayList(attractions);
            fillColumnsWithData();
            crudAttractionView.getTableView().setItems(data);
            System.out.println(attractions); // dbg
            crudAttractionView.getTableView().setDisable(false);
            //retrieving = false;
        } else {
            CrudDialoger.showAlertDialog("Non sono state trovate attrazioni con questi criteri: " + query +
                    "&page=" + currentPage + "&size=" + currentPageSize);
        }
        disableCRUDButtons();
    }

    private String getQuery(String query) {
        final String nome = getNome();
        final String numeroDiTelefono = getNumeroDiTelefono();
        final String tipoIndirizzo = getTipoIndirizzo();
        final String strada = getStrada();
        final String civico = getNumeroCivico();
        final String città = getCittà();
        final String cap = getCAP();
        final String provincia = getProvincia();
        final String prezzoMedio = getPrezzoMedio();
        String certificatoDiEccellenza = String.valueOf(crudAttractionView.getCheckBoxCertificatoDiEccellenza().isSelected());
        /*final String orarioAperturaMattutina = getOrarioAperturaMattutina();
        final String orarioChiusuraMattutina = getOrarioChiusuraMattutina();
        final String orarioAperturaSerale = getOrarioAperturaSerale();
        final String orarioChiusuraSerale = getOrarioChiusuraSerale();*/
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
        if (!città.isEmpty()) {
            if (concatena)
                query += ";address.city==\"" + città + "\"";
            else {
                query += "address.city==\"" + città + "\"";
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

        // Se selezionato il certificato di eccellenza, inseriscilo
        if (crudAttractionView.getCheckBoxCertificatoDiEccellenza().isSelected()) {
            if (concatena)
                query += ";certificateOfExcellence==\"" + certificatoDiEccellenza + "\"";
            else {
                query += "certificateOfExcellence==\"" + certificatoDiEccellenza + "\"";
                concatena = true;
            }
        } else {
            if (!CrudDialoger.ignoreExcellence()) {
                certificatoDiEccellenza = "false";
                if (concatena)
                    query += ";certificateOfExcellence==\"" + certificatoDiEccellenza + "\"";
                else {
                    query += "certificateOfExcellence==\"" + certificatoDiEccellenza + "\"";
                    concatena = true;
                }
            }
        }

        crudAttractionView.getComboBoxOrarioAperturaMattutina().setDisable(true);
        crudAttractionView.getComboBoxOrarioChiusuraMattutina().setDisable(true);
        crudAttractionView.getComboBoxOrarioAperturaSerale().setDisable(true);
        crudAttractionView.getComboBoxOrarioChiusuraSerale().setDisable(true);
        // Bug
        /*if (orarioAperturaMattutina != null && orarioChiusuraMattutina != null && orarioAperturaSerale != null && orarioChiusuraSerale != null) {
            String openingTime = orarioAperturaMattutina + " - " + orarioChiusuraMattutina +
                    " " + orarioAperturaSerale + " - " + orarioChiusuraSerale;
            if (concatena)
                query += ";openingTime==\"" + openingTime + "\"";
            else
                query += "openingTime==\"" + openingTime + "\"";
        }*/

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

    private void buttonAiutoClicked() {
        crudAttractionView.getButtonAiuto().setOnAction(event -> CrudDialoger.showHelpDialog());
    }

    private void buttonCaricaClicked() {
        crudAttractionView.getButtonCaricaFoto().setOnAction(event -> multiFileSelectionFromFileSystem());
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
                crudAttractionView.getListViewFotoPath().getItems().add(selectedFile.getAbsolutePath());
            }
        } else {
            CrudDialoger.showAlertDialog("I file selezionati non sono validi");
        }
    }

    private void buttonEliminaFotoSelezionataClicked() {
        crudAttractionView.getButtonEliminaFotoSelezionate().setOnAction(event -> {
            ObservableList<String> imagesSelectedToDelete2 = crudAttractionView.getListViewFotoPath().getSelectionModel().getSelectedItems();
            imagesSelectedToDelete = FXCollections.observableArrayList(imagesSelectedToDelete2);
            crudAttractionView.getListViewFotoPath().getItems().removeAll(imagesSelectedToDelete2);
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
                crudAttractionView.getTextFieldCittà().getText(),
                crudAttractionView.getTextFieldProvincia().getText(),
                crudAttractionView.getTextFieldCAP().getText());
    }

    private String getEligibleStringAddressForGeocoding() {
        return crudAttractionView.getChoiceBoxIndirizzo().getValue() + " " + crudAttractionView.getTextFieldStrada().getText() + ", " +
                crudAttractionView.getTxtFieldNumeroCivico().getText() + ", " + crudAttractionView.getTextFieldCittà().getText() + ", " + crudAttractionView.getTextFieldCAP().getText() +
                ", " + crudAttractionView.getTextFieldProvincia().getText();
    }

    private String getCurrentDate() {
        return new Date().toString();
    }

    private String getOpeningTimeWithFormData() {
        return crudAttractionView.getComboBoxOrarioAperturaMattutina().getValue() + " - " + crudAttractionView.getComboBoxOrarioChiusuraMattutina().getValue() +
                " " + crudAttractionView.getComboBoxOrarioAperturaSerale().getValue() + " - " + crudAttractionView.getComboBoxOrarioChiusuraSerale().getValue();
    }

    private void buttonAnnullaClicked() {
        crudAttractionView.getButtonAnnulla().setOnAction(event -> {
            setViewsAsDefault();
            retrieving = false;
        });
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

    public String getCittà() {
        return this.crudAttractionView.getTextFieldCittà().getText();
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

    public String getOrarioAperturaMattutina() {
        return this.crudAttractionView.getComboBoxOrarioAperturaMattutina().getValue();
    }

    public String getOrarioChiusuraMattutina() {
        return this.crudAttractionView.getComboBoxOrarioChiusuraMattutina().getValue();
    }

    public String getOrarioAperturaSerale() {
        return this.crudAttractionView.getComboBoxOrarioAperturaSerale().getValue();
    }

    public String getOrarioChiusuraSerale() {
        return this.crudAttractionView.getComboBoxOrarioChiusuraSerale().getValue();
    }

    public Attraction getSelectedAttractionFromTableView() {
        return (Attraction) crudAttractionView.getTableView().getSelectionModel().getSelectedItem();
    }

    public CrudAttractionView getCrudAttractionView() {
        return crudAttractionView;
    }
}
