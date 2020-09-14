package controllers;

import controllers_utils.CrudDialoger;
import controllers_utils.InputValidator;
import dao_interfaces.CityDAO;
import dao_interfaces.HotelDAO;
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
import models.Hotel;
import models_helpers.Point;
import utils.ConfigFileReader;
import views.CrudHotelView;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public class CrudHotelController extends CrudController {
    private final CrudHotelView crudHotelView;
    private final int currentPageSize = 100;
    private DAOFactory daoFactory;
    private HotelDAO hotelDAO;
    private ImageDAO imageDAO;
    private int currentPage = 0;
    private ObservableList<String> imagesSelectedToDelete;
    private FormCheckerFactory formCheckerFactory;
    private FormChecker formChecker;
    private boolean retrieving = false;

    public CrudHotelController(CrudHotelView crudHotelView) {
        this.crudHotelView = crudHotelView;
    }

    @Override
    public Stage getStage() {
        return (Stage) this.getCrudHotelView().getRootPane().getScene().getWindow();
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

    private void buttonCercaClicked() {
        crudHotelView.getButtonCerca().setOnAction(e -> {
            retrieving = true;
            enableAllTextFields();
            enableAllChoiceBoxes();
            disableCRUDButtons();
            clearTextFields();
            crudHotelView.getTableView().setDisable(true);
            crudHotelView.getButtonConferma().setDisable(false);
            crudHotelView.getButtonAnnulla().setDisable(false);
            crudHotelView.getButtonIndietro().setDisable(true);
            crudHotelView.getButtonCaricaFoto().setDisable(false);
            crudHotelView.getTextFieldNumeroDiTelefono().setDisable(false);
            crudHotelView.getListViewFotoPath().setDisable(false);
            crudHotelView.getButtonCaricaFoto().setDisable(true);
            crudHotelView.getListViewFotoPath().setDisable(true);
        });
    }

    @Override
    public void setListenerOnTableView(TableView<Object> tableView) {
        if (tableView.getId().equals("tableView")) {
            tableViewClicked();
        }
    }

    @Override
    public void setViewsAsDefault() {
        crudHotelView.getButtonIndietro().setDisable(false);
        crudHotelView.getButtonInserisci().setDisable(false);
        crudHotelView.getButtonModifica().setDisable(true);
        crudHotelView.getTextFieldNumeroDiTelefono().setDisable(true);
        crudHotelView.getTextFieldStrada().setDisable(true);
        crudHotelView.getTxtFieldNumeroCivico().setDisable(true);
        crudHotelView.getTextFieldProvincia().setDisable(true);
        crudHotelView.getButtonElimina().setDisable(true);
        crudHotelView.getButtonConferma().setDisable(true);
        crudHotelView.getButtonAnnulla().setDisable(true);
        crudHotelView.getButtonAiuto().setDisable(false);
        crudHotelView.getButtonAiuto().setDisable(false);
        crudHotelView.getTextFieldNome().setDisable(true);
        crudHotelView.getTextFieldPrezzoMedio().setDisable(true);
        crudHotelView.getChoiceBoxIndirizzo().setDisable(true);
        crudHotelView.getTextFieldCAP().setDisable(true);
        crudHotelView.getTextFieldCittà().setDisable(true);
        crudHotelView.getCheckBoxCertificatoDiEccellenza().setDisable(true);
        crudHotelView.getButtonCaricaFoto().setDisable(true);
        crudHotelView.getListViewFotoPath().setDisable(true);
        crudHotelView.getListViewFotoPath().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        crudHotelView.getListViewFotoPath().getItems().clear();
        crudHotelView.getButtonEliminaFotoSelezionate().setDisable(true);
        crudHotelView.getChoiceBoxNumeroStelle().setDisable(true);
        crudHotelView.getTableView().setDisable(false);
        crudHotelView.getButtonCerca().setDisable(false);
        crudHotelView.getCheckBoxCertificatoDiEccellenza().setSelected(false);
        initializeBoxes();
        crudHotelView.getChoiceBoxIndirizzo().getSelectionModel().clearSelection();
        clearTextFields();
        loadHotelsIntoTableView(currentPage, currentPageSize);
    }

    @Override
    public void setListenerOnListView(ListView<String> listViewFotoPath) {
        if (listViewFotoPath.getId().equals("listViewFotoPath")) {
            listViewFotoPathClicked();
        }
    }

    private void listViewFotoPathClicked() {
        crudHotelView.getListViewFotoPath().setOnMouseClicked(event -> {
            if (!crudHotelView.getListViewFotoPath().getSelectionModel().getSelectedItems().isEmpty())
                crudHotelView.getButtonEliminaFotoSelezionate().setDisable(false);
        });
    }

    public void buttonIndietroClicked() {
        crudHotelView.getButtonIndietro().setOnAction(event -> showSelectTypeStage());
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
        crudHotelView.getButtonInserisci().setOnAction(event -> {
            enableAllTextFields();
            enableAllChoiceBoxes();
            disableCRUDButtons();
            crudHotelView.getTableView().setDisable(true);
            crudHotelView.getButtonConferma().setDisable(false);
            crudHotelView.getButtonAnnulla().setDisable(false);
            crudHotelView.getButtonIndietro().setDisable(true);
            crudHotelView.getButtonCaricaFoto().setDisable(false);
            crudHotelView.getTextFieldNumeroDiTelefono().setDisable(false);
            crudHotelView.getListViewFotoPath().setDisable(false);
            crudHotelView.getChoiceBoxNumeroStelle().setDisable(false);
        });
    }

    public void enableAllTextFields() {
        crudHotelView.getTextFieldCAP().setDisable(false);
        crudHotelView.getTextFieldCittà().setDisable(false);
        crudHotelView.getTextFieldNome().setDisable(false);
        crudHotelView.getTextFieldNumeroDiTelefono().setDisable(false);
        crudHotelView.getTextFieldPrezzoMedio().setDisable(false);
        crudHotelView.getTextFieldStrada().setDisable(false);
        crudHotelView.getTxtFieldNumeroCivico().setDisable(false);
        crudHotelView.getTextFieldProvincia().setDisable(false);
    }

    public void enableAllChoiceBoxes() {
        crudHotelView.getCheckBoxCertificatoDiEccellenza().setDisable(false);
        crudHotelView.getChoiceBoxIndirizzo().setDisable(false);
        crudHotelView.getChoiceBoxNumeroStelle().setDisable(false);
    }

    public void disableCRUDButtons() {
        crudHotelView.getButtonCerca().setDisable(true);
        crudHotelView.getButtonInserisci().setDisable(true);
        crudHotelView.getButtonModifica().setDisable(true);
        crudHotelView.getButtonElimina().setDisable(true);
    }

    public void buttonModificaClicked() {
        crudHotelView.getButtonModifica().setOnAction(event -> {
            crudHotelView.getButtonInserisci().setDisable(true);
            crudHotelView.getButtonConferma().setDisable(false);
            crudHotelView.getButtonAnnulla().setDisable(false);
            enableAllTextFields();
            enableAllChoiceBoxes();
            crudHotelView.getButtonCaricaFoto().setDisable(false);
            crudHotelView.getListViewFotoPath().setDisable(false);
        });
    }

    public void buttonEliminaClicked() {
        crudHotelView.getButtonElimina().setOnAction(event -> {
            daoFactory = DAOFactory.getInstance();
            hotelDAO = daoFactory.getHotelDAO(ConfigFileReader.getProperty("hotel_storage_technology"));
            Hotel selectedHotel = getSelectedHotelFromTableView();
            if (selectedHotel != null) {
                if (CrudDialoger.areYouSureToDelete(this, selectedHotel.getName())) {
                    try {
                        if (!hotelDAO.delete(selectedHotel))
                            CrudDialoger.showAlertDialog(this,
                                    "Qualcosa è andato storto durante la cancellazione");
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
        initializeChoiceBoxNumeroStelle();
    }

    private void clearTextFields() {
        crudHotelView.getTextFieldNome().setText("");
        crudHotelView.getTextFieldCAP().setText("");
        crudHotelView.getTextFieldCittà().setText("");
        crudHotelView.getTextFieldPrezzoMedio().setText("");
        crudHotelView.getTextFieldNumeroDiTelefono().setText("");
        crudHotelView.getTextFieldStrada().setText("");
        crudHotelView.getTxtFieldNumeroCivico().setText("");
        crudHotelView.getTextFieldProvincia().setText("");
    }

    private void initializeChoiceBoxIndirizzo() {
        ObservableList<String> observableList = FXCollections.observableArrayList("Via", "Viale", "Vico", "Piazza", "Largo");
        crudHotelView.getChoiceBoxIndirizzo().setItems(observableList);
        //crudHotelView.getChoiceBoxIndirizzo().getSelectionModel().selectFirst();
    }

    private void initializeChoiceBoxNumeroStelle() {
        ObservableList<Integer> observableList = FXCollections.observableArrayList(Integer.parseInt("1"),
                Integer.parseInt("2"),
                Integer.parseInt("3"),
                Integer.parseInt("4"),
                Integer.parseInt("5"));
        crudHotelView.getChoiceBoxNumeroStelle().setItems(observableList);
        crudHotelView.getChoiceBoxIndirizzo().getSelectionModel().selectFirst();
    }

    public void loadHotelsIntoTableView(int page, int size) {
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

    private void tableViewClicked() {
        crudHotelView.getTableView().setOnMouseClicked(event -> {
            Hotel selectedHotel = getSelectedHotelFromTableView();
            if (selectedHotel != null) {
                crudHotelView.getButtonAnnulla().setDisable(false);
                crudHotelView.getButtonElimina().setDisable(false);
                crudHotelView.getButtonModifica().setDisable(false);
                crudHotelView.getListViewFotoPath().getItems().clear();
                populateTextFieldsWithSelectedHotelData(selectedHotel);
            }
        });
    }

    private void populateTextFieldsWithSelectedHotelData(Hotel hotel) {
        crudHotelView.getTextFieldNome().setText(hotel.getName());
        crudHotelView.getTextFieldNumeroDiTelefono().setText(hotel.getPhoneNumber());
        setProperAddressTypeIntoAddressTypeChoiceBox(hotel);
        setProperStarsIntoStarsChoiceBox(hotel);
        crudHotelView.getTextFieldStrada().setText(hotel.getStreet());
        crudHotelView.getTxtFieldNumeroCivico().setText(hotel.getHouseNumber());
        crudHotelView.getTextFieldCittà().setText(hotel.getCity());
        crudHotelView.getTextFieldCAP().setText(hotel.getPostalCode());
        crudHotelView.getTextFieldProvincia().setText(hotel.getProvince());
        crudHotelView.getTextFieldPrezzoMedio().setText(String.valueOf(hotel.getAvaragePrice()));
        crudHotelView.getCheckBoxCertificatoDiEccellenza().setSelected(hotel.isHasCertificateOfExcellence());
        setProperImagesIntoListView(hotel);
    }

    private void setProperAddressTypeIntoAddressTypeChoiceBox(Hotel hotel) {
        if (hotel.getTypeOfAddress().equals("Via"))
            crudHotelView.getChoiceBoxIndirizzo().getSelectionModel().select(0);
        if (hotel.getTypeOfAddress().equals("Viale"))
            crudHotelView.getChoiceBoxIndirizzo().getSelectionModel().select(1);
        if (hotel.getTypeOfAddress().equals("Vico"))
            crudHotelView.getChoiceBoxIndirizzo().getSelectionModel().select(2);
        if (hotel.getTypeOfAddress().equals("Piazza"))
            crudHotelView.getChoiceBoxIndirizzo().getSelectionModel().select(3);
        if (hotel.getTypeOfAddress().equals("Largo"))
            crudHotelView.getChoiceBoxIndirizzo().getSelectionModel().select(4);
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

    private void setProperImagesIntoListView(Hotel hotel) {
        if (hotel.getImages() != null) {
            for (String image : hotel.getImages()) {
                crudHotelView.getListViewFotoPath().getItems().add(image);
            }
        }
    }

    private void buttonMostraAvantiClicked() {
        crudHotelView.getButtonMostraAvanti().setOnAction(event -> {
            System.out.println("Premuto Mostra avanti");
            if (!crudHotelView.getTableView().getItems().isEmpty()) {
                currentPage++;
                loadHotelsIntoTableView(currentPage, currentPageSize);
            }
        });
    }

    private void buttonMostraIndietroClicked() {
        crudHotelView.getButtonMostraIndietro().setOnAction(event -> {
            System.out.println("Premuto mostra indietro");
            if (currentPage != 0) {
                currentPage--;
                loadHotelsIntoTableView(currentPage, currentPageSize);
            }
        });
    }

    private void buttonConfermaClicked() {
        crudHotelView.getButtonConferma().setOnAction(event -> {
            String telephoneNumber = getNumeroDiTelefono();
            String prezzoMedio = getPrezzoMedio();
            String numeroCivico = getNumeroCivico();
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
                    CrudDialoger.showAlertDialog(this, "Riempi tutti i campi");
                } else {
                    if (InputValidator.isValidTelephoneNumber(telephoneNumber)) {
                        if (InputValidator.isNumberGreaterOrEqualToZero(prezzoMedio)) {
                            if (InputValidator.isNumberGreaterOrEqualToZero(numeroCivico)) {
                                if (InputValidator.isNumberGreaterOrEqualToZero(cap)) {
                                    Hotel hotel = getHotelWithFormData();
                                    daoFactory = DAOFactory.getInstance();
                                    hotelDAO = daoFactory.getHotelDAO(ConfigFileReader.getProperty("hotel_storage_technology"));
                                    if (crudHotelView.getButtonModifica().isDisable()) {
                                        doInsert(hotel);
                                    } else {
                                        doUpdate(hotel);
                                        imagesSelectedToDelete = null;
                                    }
                                } else {
                                    CrudDialoger.showAlertDialog(this, "CAP non valido");
                                }
                            } else {
                                CrudDialoger.showAlertDialog(this, "Numero civico non valido");
                            }
                        } else {
                            CrudDialoger.showAlertDialog(this, "Prezzo medio non valido. Inserire un intero");
                        }
                    } else {
                        CrudDialoger.showAlertDialog(this, "Numero di telefono non valido");
                    }
                }
            }
        });
    }

    private void doRetrieveByQuery() throws IOException, InterruptedException {
        String query = "";

        query = getQuery(query);

        CrudDialoger.showAlertDialog(this, query); // dbg

        daoFactory = DAOFactory.getInstance();
        hotelDAO = daoFactory.getHotelDAO(ConfigFileReader.getProperty("hotel_storage_technology"));

        List<Hotel> hotels = (query.equals("")) ? hotelDAO.retrieveAt(currentPage, currentPageSize)
                : hotelDAO.retrieveByQuery(query, currentPage, currentPageSize);

        if (hotels != null) {
            final ObservableList<Object> data = FXCollections.observableArrayList(hotels);
            fillColumnsWithData();
            crudHotelView.getTableView().setItems(data);
            System.out.println(hotels); // dbg
            crudHotelView.getTableView().setDisable(false);
            //retrieving = false;
        } else {
            CrudDialoger.showAlertDialog(this, "Non sono stati trovati hotel con questi criteri: " + query +
                    "&page=" + currentPage + "&size=" + currentPageSize);
        }
        disableCRUDButtons();
    }

    private String getQuery(String query) {
        final String nome = getNome();
        final String numeroDiTelefono = getNumeroDiTelefono();
        final String tipoIndirizzo = getTipoIndirizzo();
        final Integer stelle = getStars();
        final String strada = getStrada();
        final String civico = getNumeroCivico();
        final String città = getCittà();
        final String cap = getCAP();
        final String provincia = getProvincia();
        final String prezzoMedio = getPrezzoMedio();
        String certificatoDiEccellenza = String.valueOf(crudHotelView.getCheckBoxCertificatoDiEccellenza().isSelected());
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

        if (stelle != null) {
            if (concatena)
                query += ";stars==\"" + stelle + "\"";
            else {
                query += "stars==\"" + stelle + "\"";
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
        if (crudHotelView.getCheckBoxCertificatoDiEccellenza().isSelected()) {
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

        return query;
    }

    private void doInsert(Hotel hotel) {
        try {
            if (!hotelDAO.add(hotel))
                CrudDialoger.showAlertDialog(this, "Qualcosa è andato storto durante l'inserimento");
            else
                CrudDialoger.showAlertDialog(this, "Inserimento avvenuto");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        setViewsAsDefault();
    }

    private void doUpdate(Hotel hotel) {
        Hotel clickedHotel = (Hotel) crudHotelView.getTableView().getSelectionModel().getSelectedItem();
        hotel.setId(clickedHotel.getId());
        hotel.setReviews(clickedHotel.getReviews());
        hotel.setLastModificationDate(clickedHotel.getLastModificationDate());

        //CrudDialoger.showAlertDialog(this, "Old City: " + clickedHotel.getCity() + "\nNew City: " + hotel.getCity()); // dbg

        //CrudDialoger.showAlertDialog(this, "imagesSelectedToDelete: " + imagesSelectedToDelete); // dbg

        //System.out.println("Hotel debug: " + hotel); // dbg
        for (String image : getImagesFromListView()) {
            File file = new File(image);
            //CrudDialoger.showAlertDialog(this, image); // dbg
            if (file.isAbsolute()) {
                //File file = new File(image);
                CrudDialoger.showAlertDialog(this, image + " da inserire"); // dbg
                daoFactory = DAOFactory.getInstance();
                imageDAO = daoFactory.getImageDAO(ConfigFileReader.getProperty("image_storage_technology"));
                String imageHostUrl = null;
                try {
                    imageHostUrl = imageDAO.loadFileIntoBucket(file); // carica su s3
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    if (hotelDAO.updateHotelSingleImageFromHotelCollection(hotel, imageHostUrl) || imageHostUrl == null)
                        CrudDialoger.showAlertDialog(this, "Qualcosa è andato storto");
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            if (!hotelDAO.update(hotel))
                CrudDialoger.showAlertDialog(this, "Qualcosa è andato storto durante l'update di hotel");
            else
                updateImages(hotel, imagesSelectedToDelete);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        if (!clickedHotel.getCity().equals(hotel.getCity())) {
            //CrudDialoger.showAlertDialog(this, clickedHotel.getCity() + " not equal to " + hotel.getCity());
            daoFactory = DAOFactory.getInstance();
            CityDAO cityDAO = daoFactory.getCityDAO(ConfigFileReader.getProperty("city_storage_technology"));
            try {
                //System.out.println(clickedHotel);
                if (!cityDAO.delete(clickedHotel))
                    CrudDialoger.showAlertDialog(this, "Non è stato possibile eliminare"); // dbg
                if (!cityDAO.insert(hotel))
                    CrudDialoger.showAlertDialog(this, "Non è stato possibile inserire"); // dbg
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        setViewsAsDefault();
    }

    private void updateImages(Hotel hotel, ObservableList<String> images) throws IOException, InterruptedException {
        daoFactory = DAOFactory.getInstance();
        imageDAO = daoFactory.getImageDAO(ConfigFileReader.getProperty("image_storage_technology"));
        hotelDAO = daoFactory.getHotelDAO(ConfigFileReader.getProperty("hotel_storage_technology"));
        if (images != null) {
            for (String imageUrl : images) {
                if (!imageDAO.deleteThisImageFromBucket(imageUrl) || !hotelDAO.deleteHotelSingleImageFromHotelCollection(hotel, imageUrl))
                    CrudDialoger.showAlertDialog(this, "Modifica non avvenuta");
            }
            //CrudDialoger.showAlertDialog(this, "Modifica effettuata");
        }
    }

    private void buttonAiutoClicked() {
        crudHotelView.getButtonAiuto().setOnAction(event -> CrudDialoger.showHelpDialog(this));
    }

    private void buttonCaricaClicked() {
        crudHotelView.getButtonCaricaFoto().setOnAction(event -> multiFileSelectionFromFileSystem());
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
                crudHotelView.getListViewFotoPath().getItems().add(selectedFile.getAbsolutePath());
            }
        } else {
            CrudDialoger.showAlertDialog(this, "I file selezionati non sono validi");
        }
    }

    private void buttonEliminaFotoSelezionataClicked() {
        crudHotelView.getButtonEliminaFotoSelezionate().setOnAction(event -> {
            ObservableList<String> imagesSelectedToDelete2 = crudHotelView.getListViewFotoPath().getSelectionModel().getSelectedItems();
            imagesSelectedToDelete = FXCollections.observableArrayList(imagesSelectedToDelete2);
            crudHotelView.getListViewFotoPath().getItems().removeAll(imagesSelectedToDelete2);
        });
    }

    private Hotel getHotelWithFormData() {
        Hotel hotel = new Hotel();
        hotel.setName(crudHotelView.getTextFieldNome().getText());
        hotel.setStars(crudHotelView.getChoiceBoxNumeroStelle().getValue());
        hotel.setAddress(getAddressWithFormData());
        hotel.setAvaragePrice(Integer.parseInt(crudHotelView.getTextFieldPrezzoMedio().getText()));
        hotel.setPhoneNumber(crudHotelView.getTextFieldNumeroDiTelefono().getText());
        hotel.setHasCertificateOfExcellence(crudHotelView.getCheckBoxCertificatoDiEccellenza().isSelected());
        hotel.setImages(crudHotelView.getListViewFotoPath().getItems());
        hotel.setPoint(new Point(Geocoder.reverseGeocoding(getEligibleStringAddressForGeocoding()).getLat(),
                Geocoder.reverseGeocoding(getEligibleStringAddressForGeocoding()).getLng()));
        hotel.setAddedDate(getCurrentDate());
        return hotel;
    }

    private Address getAddressWithFormData() {
        return new Address(
                crudHotelView.getChoiceBoxIndirizzo().getValue(),
                crudHotelView.getTextFieldStrada().getText(),
                crudHotelView.getTxtFieldNumeroCivico().getText(),
                crudHotelView.getTextFieldCittà().getText(),
                crudHotelView.getTextFieldProvincia().getText(),
                crudHotelView.getTextFieldCAP().getText());
    }

    private String getEligibleStringAddressForGeocoding() {
        return crudHotelView.getChoiceBoxIndirizzo().getValue() + " " + crudHotelView.getTextFieldStrada().getText() + ", " +
                crudHotelView.getTxtFieldNumeroCivico().getText() + ", " + crudHotelView.getTextFieldCittà().getText() + ", " + crudHotelView.getTextFieldCAP().getText() +
                ", " + crudHotelView.getTextFieldProvincia().getText();
    }

    public CrudHotelView getCrudHotelView() {
        return crudHotelView;
    }

    private String getCurrentDate() {
        return new Date().toString();
    }

    private void buttonAnnullaClicked() {
        crudHotelView.getButtonAnnulla().setOnAction(event -> {
            setViewsAsDefault();
            retrieving = false;
        });
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

    public String getCittà() {
        return this.crudHotelView.getTextFieldCittà().getText();
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

    public Hotel getSelectedHotelFromTableView() {
        return (Hotel) crudHotelView.getTableView().getSelectionModel().getSelectedItem();
    }
}
