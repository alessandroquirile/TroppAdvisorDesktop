package views;

import controllers.CrudRestaurantController;
import controllers_utils.TableSettersGetters;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public class CrudRestaurantView implements Initializable {
    @FXML
    private AnchorPane rootPane;
    @FXML
    private javafx.scene.control.Button buttonElimina;
    @FXML
    private javafx.scene.control.Button buttonConferma;
    @FXML
    private javafx.scene.control.Button buttonAnnulla;
    @FXML
    private javafx.scene.control.Button buttonAiuto;
    @FXML
    private TableView<Object> tableView;
    @FXML
    private TableColumn<Object, String> tableColumnName;
    @FXML
    private TableColumn<Object, String> tableColumnId;
    @FXML
    private TableColumn<Object, String> tableColumnOrarioApertura;
    @FXML
    private TableColumn<Object, Integer> tableColumnVotoMedio;
    @FXML
    private TableColumn<Object, Integer> tableColumnPrezzoMedio;
    @FXML
    private TableColumn<Object, String> tableColumnNumeroDiTelefono;
    @FXML
    private TableColumn<Object, Boolean> tableColumnHasCertificateOfExcellence;
    @FXML
    private TableColumn<Object, Integer> tableColumnTotalReview;
    @FXML
    private TableColumn<Object, String> tableColumnAddedDate;
    @FXML
    private TableColumn<Object, String> tableColumnLastModificationDate;
    @FXML
    private TableColumn<Object, String> tableColumnIndirizzo;
    @FXML
    private TableColumn<Object, String> tableColumnTipoDiCucina;
    @FXML
    private TableColumn<Object, String> tableColumnPuntoSuMappa;
    @FXML
    private TableColumn<Object, String> tableColumnImmagini;
    @FXML
    private javafx.scene.control.Button buttonModifica;
    @FXML
    private javafx.scene.control.TextField textFieldNome;
    @FXML
    private ChoiceBox<String> choiceBoxIndirizzo;
    @FXML
    private javafx.scene.control.TextField textFieldCittà;
    @FXML
    private CheckBox checkBoxCertificatoDiEccellenza;
    @FXML
    private javafx.scene.control.Button buttonIndietro;
    @FXML
    private javafx.scene.control.Button buttonInserisci;
    @FXML
    private javafx.scene.control.TextField textFieldCAP;
    @FXML
    private TextField textFieldNumeroDiTelefono;
    @FXML
    private Button buttonCaricaFoto;
    @FXML
    private ListView<String> listViewFotoPath;
    @FXML
    private TableColumn<TableSettersGetters, String> typeOfCuisineNameColumn;
    @FXML
    private TableColumn<TableSettersGetters, CheckBox> typeOfCuisineSelectColumn;
    @FXML
    private TableView<TableSettersGetters> tableViewTypeOfCuisine;
    @FXML
    private TextField textFieldPrezzoMedio;
    @FXML
    private TextField txtFieldNumeroCivico;
    @FXML
    private TextField textFieldProvincia;
    @FXML
    private TextField textFieldStrada;
    @FXML
    private ComboBox<String> comboBoxOrarioAperturaMattutina;
    @FXML
    private ComboBox<String> comboBoxOrarioChiusuraMattutina;
    @FXML
    private ComboBox<String> comboBoxOrarioAperturaSerale;
    @FXML
    private ComboBox<String> comboBoxOrarioChiusuraSerale;
    @FXML
    private Button buttonMostraAvanti;
    @FXML
    private Button buttonMostraIndietro;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CrudRestaurantController crudRestaurantController = new CrudRestaurantController(this);
        crudRestaurantController.setListenerOn(buttonInserisci);
        crudRestaurantController.setListenerOn(buttonElimina);
        crudRestaurantController.setListenerOn(buttonModifica);
        crudRestaurantController.setListenerOn(buttonAnnulla);
        crudRestaurantController.setListenerOn(buttonIndietro);
        crudRestaurantController.setListenerOn(buttonConferma);
        crudRestaurantController.setListenerOn(buttonAiuto);
        crudRestaurantController.setListenerOn(buttonCaricaFoto);
        crudRestaurantController.setListenerOn(buttonMostraAvanti);
        crudRestaurantController.setListenerOn(buttonMostraIndietro);
        crudRestaurantController.setListenerOnTableView(tableView);
        crudRestaurantController.setViewsAsDefault();
    }

    public Button getButtonMostraIndietro() {
        return buttonMostraIndietro;
    }

    public Button getButtonMostraAvanti() {
        return buttonMostraAvanti;
    }

    public TableColumn<Object, String> getTableColumnImmagini() {
        return tableColumnImmagini;
    }

    public TableColumn<Object, String> getTableColumnPuntoSuMappa() {
        return tableColumnPuntoSuMappa;
    }

    public TableColumn<Object, String> getTableColumnTipoDiCucina() {
        return tableColumnTipoDiCucina;
    }

    public TableColumn<Object, String> getTableColumnIndirizzo() {
        return tableColumnIndirizzo;
    }

    public TableColumn<Object, String> getTableColumnLastModificationDate() {
        return tableColumnLastModificationDate;
    }

    public TableColumn<Object, String> getTableColumnAddedDate() {
        return tableColumnAddedDate;
    }

    public TableColumn<Object, Boolean> getTableColumnHasCertificateOfExcellence() {
        return tableColumnHasCertificateOfExcellence;
    }

    public TableColumn<Object, Integer> getTableColumnTotalReview() {
        return tableColumnTotalReview;
    }

    public TableColumn<Object, String> getTableColumnNumeroDiTelefono() {
        return tableColumnNumeroDiTelefono;
    }

    public TableColumn<Object, Integer> getTableColumnPrezzoMedio() {
        return tableColumnPrezzoMedio;
    }

    public TableColumn<Object, Integer> getTableColumnVotoMedio() {
        return tableColumnVotoMedio;
    }

    public TableColumn<Object, String> getTableColumnOrarioApertura() {
        return tableColumnOrarioApertura;
    }

    public TableColumn<Object, String> getTableColumnName() {
        return tableColumnName;
    }

    public TableColumn<Object, String> getTableColumnId() {
        return tableColumnId;
    }

    public AnchorPane getRootPane() {
        return rootPane;
    }

    public Button getButtonElimina() {
        return buttonElimina;
    }

    public Button getButtonConferma() {
        return buttonConferma;
    }

    public Button getButtonAnnulla() {
        return buttonAnnulla;
    }

    public Button getButtonAiuto() {
        return buttonAiuto;
    }

    public TableView<Object> getTableView() {
        return tableView;
    }

    public Button getButtonModifica() {
        return buttonModifica;
    }

    public TextField getTextFieldNome() {
        return textFieldNome;
    }

    public ChoiceBox<String> getChoiceBoxIndirizzo() {
        return choiceBoxIndirizzo;
    }

    public TextField getTextFieldCittà() {
        return textFieldCittà;
    }

    public CheckBox getCheckBoxCertificatoDiEccellenza() {
        return checkBoxCertificatoDiEccellenza;
    }

    public Button getButtonIndietro() {
        return buttonIndietro;
    }

    public Button getButtonInserisci() {
        return buttonInserisci;
    }

    public TextField getTextFieldCAP() {
        return textFieldCAP;
    }

    public TextField getTextFieldNumeroDiTelefono() {
        return textFieldNumeroDiTelefono;
    }

    public Button getButtonCaricaFoto() {
        return buttonCaricaFoto;
    }

    public ListView<String> getListViewFotoPath() {
        return listViewFotoPath;
    }

    public TableColumn<TableSettersGetters, String> getTypeOfCuisineNameColumn() {
        return typeOfCuisineNameColumn;
    }

    public TableColumn<TableSettersGetters, CheckBox> getTypeOfCuisineSelectColumn() {
        return typeOfCuisineSelectColumn;
    }

    public TableView<TableSettersGetters> getTableViewTypeOfCuisine() {
        return tableViewTypeOfCuisine;
    }

    public TextField getTextFieldPrezzoMedio() {
        return textFieldPrezzoMedio;
    }

    public TextField getTxtFieldNumeroCivico() {
        return txtFieldNumeroCivico;
    }

    public TextField getTextFieldProvincia() {
        return textFieldProvincia;
    }

    public TextField getTextFieldStrada() {
        return textFieldStrada;
    }

    public ComboBox<String> getComboBoxOrarioAperturaMattutina() {
        return comboBoxOrarioAperturaMattutina;
    }

    public ComboBox<String> getComboBoxOrarioChiusuraMattutina() {
        return comboBoxOrarioChiusuraMattutina;
    }

    public ComboBox<String> getComboBoxOrarioAperturaSerale() {
        return comboBoxOrarioAperturaSerale;
    }

    public ComboBox<String> getComboBoxOrarioChiusuraSerale() {
        return comboBoxOrarioChiusuraSerale;
    }
}