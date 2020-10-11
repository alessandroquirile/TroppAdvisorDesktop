package controllers;

import controllers_utils.Dialoger;
import dao_interfaces.CityDAO;
import dao_interfaces.ImageDAO;
import factories.DAOFactory;
import factories.FormCheckerFactory;
import factories.GeocoderFactory;
import form_checker_interfaces.FormChecker;
import geocoder_interfaces.Geocoder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Accomodation;
import utils.ConfigFileReader;
import views.CrudView;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public abstract class CrudController extends Controller {
    protected DAOFactory daoFactory;
    protected CityDAO cityDAO;
    protected int currentPage = 0;
    protected int currentPageSize = 150;
    protected FormCheckerFactory formCheckerFactory;
    protected FormChecker formChecker;
    protected GeocoderFactory geocoderFactory;
    protected Geocoder geocoder;
    protected boolean retrieving = false;
    protected boolean modifying = false;
    protected ObservableList<String> imagesSelectedToDelete;
    protected ImageDAO imageDAO;

    protected abstract void tableViewClickedEvent();

    protected abstract void buttonMostraIndietroClickedEvent();

    protected abstract void buttonMostraAvantiClickedEvent();

    protected abstract void buttonEliminaClickedEvent();

    protected abstract void buttonConfermaClickedEvent();

    public void setListenerOnButton(Button button, CrudView crudView) {
        switch (button.getId()) {
            case "buttonIndietro":
                buttonIndietroClicked(crudView);
                break;
            case "buttonInserisci":
                buttonInserisciClicked(crudView);
                break;
            case "buttonModifica":
                buttonModificaClicked(crudView);
                break;
            case "buttonElimina":
                buttonEliminaClicked(crudView);
                break;
            case "buttonConferma":
                buttonConfermaClicked(crudView);
                break;
            case "buttonAnnulla":
                buttonAnnullaClicked(crudView);
                break;
            case "buttonAiuto":
                buttonAiutoClicked(crudView);
                break;
            case "buttonCaricaFoto":
                buttonCaricaClicked(crudView);
                break;
            case "buttonEliminaFotoSelezionate":
                buttonEliminaFotoSelezionataClicked(crudView);
                break;
            case "buttonMostraIndietro":
                buttonMostraIndietroClicked(crudView);
                break;
            case "buttonMostraAvanti":
                buttonMostraAvantiClicked(crudView);
                break;
            case "buttonCerca":
                buttonCercaClicked(crudView);
                break;
        }
    }

    public void setListenerOnTableView(TableView<Object> tableView, CrudView crudView) {
        if (tableView.getId().equals("tableView"))
            tableViewClicked(crudView);
    }

    public void setListenerOnListView(ListView<String> listViewFotoPath, CrudView crudView) {
        if (listViewFotoPath.getId().equals("listViewFotoPath"))
            listViewFotoPathClicked(crudView);
    }

    public void setViewsAsDefault(CrudView crudView) {
        crudView.getButtonIndietro().setDisable(false);
        crudView.getButtonInserisci().setDisable(false);
        crudView.getButtonModifica().setDisable(true);
        crudView.getTextFieldNumeroDiTelefono().setDisable(true);
        crudView.getTextFieldStrada().setDisable(true);
        crudView.getTxtFieldNumeroCivico().setDisable(true);
        crudView.getTextFieldProvincia().setDisable(true);
        crudView.getButtonElimina().setDisable(true);
        crudView.getButtonConferma().setDisable(true);
        crudView.getButtonAnnulla().setDisable(true);
        crudView.getButtonAiuto().setDisable(false);
        crudView.getTextFieldNome().setDisable(true);
        crudView.getTextFieldPrezzoMedio().setDisable(true);
        crudView.getChoiceBoxIndirizzo().setDisable(true);
        crudView.getTextFieldCAP().setDisable(true);
        crudView.getTextFieldCity().setDisable(true);
        crudView.getCheckBoxCertificatoDiEccellenza().setDisable(true);
        crudView.getChoiceBoxIndirizzo().getSelectionModel().clearSelection();
        crudView.getButtonCaricaFoto().setDisable(true);
        crudView.getListViewFotoPath().setDisable(true);
        crudView.getListViewFotoPath().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        crudView.getListViewFotoPath().getItems().clear();
        crudView.getButtonEliminaFotoSelezionate().setDisable(true);
        crudView.getTableView().setDisable(false);
        crudView.getButtonCerca().setDisable(false);
        crudView.getCheckBoxCertificatoDiEccellenza().setSelected(false);
        initializeBoxes(crudView);
        clearTextFields(crudView);
    }

    private void tableViewClicked(CrudView crudView) {
        crudView.getTableView().setOnMouseClicked(event -> tableViewClickedEvent());
    }

    private void buttonMostraIndietroClicked(CrudView crudView) {
        crudView.getButtonMostraIndietro().setOnAction(event -> buttonMostraIndietroClickedEvent());
    }

    private void buttonMostraAvantiClicked(CrudView crudView) {
        crudView.getButtonMostraAvanti().setOnAction(event -> buttonMostraAvantiClickedEvent());
    }

    private void buttonEliminaClicked(CrudView crudView) {
        crudView.getButtonElimina().setOnAction(event -> buttonEliminaClickedEvent());
    }

    protected void buttonConfermaClicked(CrudView crudView) {
        crudView.getButtonConferma().setOnAction(event -> buttonConfermaClickedEvent());
    }

    private void buttonIndietroClicked(CrudView crudView) {
        crudView.getButtonIndietro().setOnAction(event -> showSelectCollectionTypeView());
    }

    private void buttonInserisciClicked(CrudView crudView) {
        crudView.getButtonInserisci().setOnAction(event -> buttonInserisciClickedEvent(crudView));
    }

    protected void buttonInserisciClickedEvent(CrudView crudView) {
        enableAllTextFields(crudView);
        enableAllChoiceBoxes(crudView);
        disableCRUDButtons(crudView);
        crudView.getTableView().setDisable(true);
        crudView.getButtonConferma().setDisable(false);
        crudView.getButtonAnnulla().setDisable(false);
        crudView.getButtonIndietro().setDisable(true);
        crudView.getButtonCaricaFoto().setDisable(false);
        crudView.getListViewFotoPath().setDisable(false);
    }

    private void buttonModificaClicked(CrudView crudView) {
        crudView.getButtonModifica().setOnAction(event -> {
            modifying = true;
            retrieving = false;
            crudView.getButtonInserisci().setDisable(true);
            crudView.getButtonConferma().setDisable(false);
            crudView.getButtonAnnulla().setDisable(false);
            crudView.getButtonCerca().setDisable(true);
            crudView.getButtonModifica().setDisable(true);
            enableAllTextFields(crudView);
            enableAllChoiceBoxes(crudView);
            crudView.getButtonCaricaFoto().setDisable(false);
            crudView.getListViewFotoPath().setDisable(false);
        });
    }

    private void buttonAnnullaClicked(CrudView crudView) {
        crudView.getButtonAnnulla().setOnAction(event -> {
            setViewsAsDefault(crudView);
            retrieving = false;
            modifying = false;
        });
    }

    private void buttonAiutoClicked(CrudView crudView) {
        crudView.getButtonAiuto().setOnAction(event -> Dialoger.showHelpDialog());
    }

    private void buttonCaricaClicked(CrudView crudView) {
        crudView.getButtonCaricaFoto().setOnAction(event -> multiFileSelectionFromFileSystem(crudView));
    }

    private void buttonEliminaFotoSelezionataClicked(CrudView crudView) {
        crudView.getButtonEliminaFotoSelezionate().setOnAction(event -> {
            ObservableList<String> images = crudView.getListViewFotoPath().getSelectionModel().getSelectedItems();
            imagesSelectedToDelete = FXCollections.observableArrayList(images);
            crudView.getListViewFotoPath().getItems().removeAll(images);
        });
    }

    private void buttonCercaClicked(CrudView crudView) {
        crudView.getButtonCerca().setOnAction(event -> buttonCercaClickedEvent(crudView));
    }

    protected void buttonCercaClickedEvent(CrudView crudView) {
        retrieving = true;
        enableAllTextFields(crudView);
        enableAllChoiceBoxes(crudView);
        disableCRUDButtons(crudView);
        clearTextFields(crudView);
        crudView.getTableView().setDisable(true);
        crudView.getButtonConferma().setDisable(false);
        crudView.getButtonAnnulla().setDisable(false);
        crudView.getButtonIndietro().setDisable(true);
        crudView.getButtonCaricaFoto().setDisable(false);
        crudView.getListViewFotoPath().setDisable(false);
        crudView.getButtonCaricaFoto().setDisable(true);
        crudView.getListViewFotoPath().setDisable(true);
        crudView.getChoiceBoxIndirizzo().getSelectionModel().clearSelection();
    }

    protected void initializeBoxes(CrudView crudView) {
        initializeChoiceBoxIndirizzo(crudView);
    }

    protected void showSelectCollectionTypeView() {
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

    private void closeCurrentStage() {
        getStage().close();
    }

    private void listViewFotoPathClicked(CrudView crudView) {
        crudView.getListViewFotoPath().setOnMouseClicked(event -> {
            if (!crudView.getListViewFotoPath().getSelectionModel().getSelectedItems().isEmpty())
                crudView.getButtonEliminaFotoSelezionate().setDisable(false);
        });
    }

    protected void enableAllTextFields(CrudView crudView) {
        crudView.getTextFieldCAP().setDisable(false);
        crudView.getTextFieldCity().setDisable(false);
        crudView.getTextFieldNome().setDisable(false);
        crudView.getTextFieldNumeroDiTelefono().setDisable(false);
        crudView.getTextFieldPrezzoMedio().setDisable(false);
        crudView.getTextFieldStrada().setDisable(false);
        crudView.getTxtFieldNumeroCivico().setDisable(false);
        crudView.getTextFieldProvincia().setDisable(false);
    }

    protected void enableAllChoiceBoxes(CrudView crudView) {
        crudView.getCheckBoxCertificatoDiEccellenza().setDisable(false);
        crudView.getChoiceBoxIndirizzo().setDisable(false);
    }

    protected void disableCRUDButtons(CrudView crudView) {
        crudView.getButtonCerca().setDisable(true);
        crudView.getButtonInserisci().setDisable(true);
        crudView.getButtonModifica().setDisable(true);
        crudView.getButtonElimina().setDisable(true);
    }

    protected void clearTextFields(CrudView crudView) {
        crudView.getTextFieldNome().setText("");
        crudView.getTextFieldCAP().setText("");
        crudView.getTextFieldCity().setText("");
        crudView.getTextFieldPrezzoMedio().setText("");
        crudView.getTextFieldNumeroDiTelefono().setText("");
        crudView.getTextFieldStrada().setText("");
        crudView.getTxtFieldNumeroCivico().setText("");
        crudView.getTextFieldProvincia().setText("");
    }

    protected void initializeChoiceBoxIndirizzo(CrudView crudView) {
        ObservableList<String> observableList = FXCollections.observableArrayList("Via", "Viale", "Vico", "Piazza", "Largo");
        crudView.getChoiceBoxIndirizzo().setItems(observableList);
    }

    protected void setProperAddressTypeIntoChoiceBox(Accomodation accomodation, CrudView crudView) {
        if (accomodation.getTypeOfAddress().equals("Via"))
            crudView.getChoiceBoxIndirizzo().getSelectionModel().select(0);
        if (accomodation.getTypeOfAddress().equals("Viale"))
            crudView.getChoiceBoxIndirizzo().getSelectionModel().select(1);
        if (accomodation.getTypeOfAddress().equals("Vico"))
            crudView.getChoiceBoxIndirizzo().getSelectionModel().select(2);
        if (accomodation.getTypeOfAddress().equals("Piazza"))
            crudView.getChoiceBoxIndirizzo().getSelectionModel().select(3);
        if (accomodation.getTypeOfAddress().equals("Largo"))
            crudView.getChoiceBoxIndirizzo().getSelectionModel().select(4);
    }

    protected void setProperImagesIntoListView(Accomodation accomodation, CrudView crudView) {
        if (accomodation.getImages() != null) {
            for (String image : accomodation.getImages())
                crudView.getListViewFotoPath().getItems().add(image);
        }
    }

    protected void populateTextFieldsWithSelectedAccomodationData(Accomodation accomodation, CrudView crudView) {
        crudView.getTextFieldNome().setText(accomodation.getName());
        crudView.getTextFieldNumeroDiTelefono().setText(accomodation.getPhoneNumber());
        setProperAddressTypeIntoChoiceBox(accomodation, crudView);
        crudView.getTextFieldStrada().setText(accomodation.getStreet());
        crudView.getTxtFieldNumeroCivico().setText(accomodation.getHouseNumber());
        crudView.getTextFieldCity().setText(accomodation.getCity());
        crudView.getTextFieldCAP().setText(accomodation.getPostalCode());
        crudView.getTextFieldProvincia().setText(accomodation.getProvince());
        crudView.getTextFieldPrezzoMedio().setText(String.valueOf(accomodation.getAvaragePrice()));
        crudView.getCheckBoxCertificatoDiEccellenza().setSelected(accomodation.isHasCertificateOfExcellence());
        setProperImagesIntoListView(accomodation, crudView);
    }

    private void multiFileSelectionFromFileSystem(CrudView crudView) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        FileChooser.ExtensionFilter imageFilter =
                new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.jpeg");
        fileChooser.getExtensionFilters().add(imageFilter);
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(null);
        if (selectedFiles != null) {
            for (File selectedFile : selectedFiles)
                crudView.getListViewFotoPath().getItems().add(selectedFile.getAbsolutePath());
        }
    }

    private String getAddressByFormData(CrudView crudView) {
        return
                crudView.getChoiceBoxIndirizzo().getValue() + ", " +
                        crudView.getTextFieldStrada().getText() + ", " +
                        crudView.getTxtFieldNumeroCivico().getText() + ", " +
                        crudView.getTextFieldCity().getText() + ", " +
                        crudView.getTextFieldProvincia().getText() + ", " +
                        crudView.getTextFieldCAP().getText();
    }

    protected Accomodation getSelectedAccomodationFromTableView(CrudView crudView) {
        return (Accomodation) crudView.getTableView().getSelectionModel().getSelectedItem();
    }

    protected Accomodation getAccomodationByFormData(CrudView crudView) {
        Accomodation accomodation = new Accomodation();
        accomodation.setName(crudView.getTextFieldNome().getText());
        accomodation.setAddressByString(getAddressByFormData(crudView));
        accomodation.setAvaragePrice(Integer.parseInt(crudView.getTextFieldPrezzoMedio().getText()));
        accomodation.setPhoneNumber(crudView.getTextFieldNumeroDiTelefono().getText());
        accomodation.setHasCertificateOfExcellence(crudView.getCheckBoxCertificatoDiEccellenza().isSelected());
        accomodation.setImages(crudView.getListViewFotoPath().getItems());
        geocoderFactory = GeocoderFactory.getInstance();
        geocoder = geocoderFactory.getGeocoder(ConfigFileReader.getProperty("geocoder_technology"));
        accomodation.setPoint(
                geocoder.forward(getAddressByFormData(crudView)).getLatitude(),
                geocoder.forward(getAddressByFormData(crudView)).getLongitude());
        accomodation.setAddedDate(getCurrentDate());
        return accomodation;
    }

    protected String getRESTQuery(CrudView crudView) {
        String query = "";
        final String nome = getNome(crudView);
        final String numeroDiTelefono = getNumeroDiTelefono(crudView);
        final String tipoIndirizzo = getTipoIndirizzo(crudView);
        final String strada = getStrada(crudView);
        final String civico = getNumeroCivico(crudView);
        final String city = getCity(crudView);
        final String cap = getCAP(crudView);
        final String provincia = getProvincia(crudView);
        final String prezzoMedio = getPrezzoMedio(crudView);
        String certificatoDiEccellenza = String.valueOf(crudView.getCheckBoxCertificatoDiEccellenza().isSelected());
        boolean concatenate = false;

        if (!nome.isEmpty()) {
            query += "name==\"" + nome + "\"";
            concatenate = true;
        }

        if (!numeroDiTelefono.isEmpty()) {
            if (concatenate)
                query += ";phoneNumber==\"" + numeroDiTelefono + "\"";
            else {
                query += "phoneNumber==\"" + numeroDiTelefono + "\"";
                concatenate = true;
            }
        }

        if (tipoIndirizzo != null) {
            if (concatenate)
                query += ";address.type==\"" + tipoIndirizzo + "\"";
            else {
                query += "address.type==\"" + tipoIndirizzo + "\"";
                concatenate = true;
            }
        }

        if (!strada.isEmpty()) {
            if (concatenate)
                query += ";address.street==\"" + strada + "\"";
            else {
                query += "address.street==\"" + strada + "\"";
                concatenate = true;
            }
        }

        if (!civico.isEmpty()) {
            if (concatenate)
                query += ";address.houseNumber==\"" + civico + "\"";
            else {
                query += "address.houseNumber==\"" + civico + "\"";
                concatenate = true;
            }
        }

        if (!city.isEmpty()) {
            if (concatenate)
                query += ";address.city==\"" + city + "\"";
            else {
                query += "address.city==\"" + city + "\"";
                concatenate = true;
            }
        }

        if (!cap.isEmpty()) {
            if (concatenate)
                query += ";address.postalCode==\"" + cap + "\"";
            else {
                query += "address.postalCode==\"" + cap + "\"";
                concatenate = true;
            }
        }

        if (!provincia.isEmpty()) {
            if (concatenate)
                query += ";address.province==\"" + provincia + "\"";
            else {
                query += "address.province==\"" + provincia + "\"";
                concatenate = true;
            }
        }

        if (!prezzoMedio.isEmpty()) {
            if (concatenate)
                query += ";avaragePrice==\"" + prezzoMedio + "\"";
            else {
                query += "avaragePrice==\"" + prezzoMedio + "\"";
                concatenate = true;
            }
        }

        if (crudView.getCheckBoxCertificatoDiEccellenza().isSelected()) {
            if (concatenate)
                query += ";certificateOfExcellence==\"" + certificatoDiEccellenza + "\"";
            else
                query += "certificateOfExcellence==\"" + certificatoDiEccellenza + "\"";
        } else {
            if (!Dialoger.ignoreExcellence()) {
                certificatoDiEccellenza = "false";
                if (concatenate)
                    query += ";certificateOfExcellence==\"" + certificatoDiEccellenza + "\"";
                else
                    query += "certificateOfExcellence==\"" + certificatoDiEccellenza + "\"";
            }
        }
        return query;
    }

    protected boolean hasChangedCity(Accomodation clickedAccomodation, Accomodation accomodation) {
        return !clickedAccomodation.getCity().equals(accomodation.getCity());
    }

    protected boolean hasToBeInserted(File file) {
        return file.isAbsolute();
    }

    protected String getCurrentDate() {
        return new Date().toString();
    }

    public String getNome(CrudView crudView) {
        return crudView.getTextFieldNome().getText();
    }

    public String getStrada(CrudView crudView) {
        return crudView.getTextFieldStrada().getText();
    }

    public String getNumeroCivico(CrudView crudView) {
        return crudView.getTxtFieldNumeroCivico().getText();
    }

    public String getTipoIndirizzo(CrudView crudView) {
        return crudView.getChoiceBoxIndirizzo().getValue();
    }

    public String getProvincia(CrudView crudView) {
        return crudView.getTextFieldProvincia().getText();
    }

    public String getCAP(CrudView crudView) {
        return crudView.getTextFieldCAP().getText();
    }

    public String getCity(CrudView crudView) {
        return crudView.getTextFieldCity().getText();
    }

    public String getPrezzoMedio(CrudView crudView) {
        return crudView.getTextFieldPrezzoMedio().getText();
    }

    public String getNumeroDiTelefono(CrudView crudView) {
        return crudView.getTextFieldNumeroDiTelefono().getText();
    }
}