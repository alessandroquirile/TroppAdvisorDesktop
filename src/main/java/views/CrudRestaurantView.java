package views;

import controllers.CrudRestaurantController;
import controllers_utils.TypeOfCuisineItem;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public class CrudRestaurantView extends FormView {
    // Ha solo in più il tipo di cucina e gli orari d'apertura mattutini e pomeridiani
    @FXML
    private TableColumn<Object, String> tableColumnTipoDiCucina;
    @FXML
    private TableColumn<TypeOfCuisineItem, String> typeOfCuisineNameColumn;
    @FXML
    private TableColumn<TypeOfCuisineItem, CheckBox> typeOfCuisineSelectColumn;
    @FXML
    private TableView<TypeOfCuisineItem> tableViewTypeOfCuisine;
    @FXML
    private TableColumn<Object, String> tableColumnOrarioApertura;
    @FXML
    private ComboBox<String> comboBoxOrarioAperturaMattutina;
    @FXML
    private ComboBox<String> comboBoxOrarioChiusuraMattutina;
    @FXML
    private ComboBox<String> comboBoxOrarioAperturaSerale;
    @FXML
    private ComboBox<String> comboBoxOrarioChiusuraSerale;

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
        crudRestaurantController.setListenerOn(buttonEliminaFotoSelezionate);
        crudRestaurantController.setListenerOnTableView(tableView);
        crudRestaurantController.setListenerOnListView(listViewFotoPath);
        crudRestaurantController.setViewsAsDefault();
    }

    public TableColumn<Object, String> getTableColumnTipoDiCucina() {
        return tableColumnTipoDiCucina;
    }

    public TableColumn<Object, String> getTableColumnOrarioApertura() {
        return tableColumnOrarioApertura;
    }

    public TableColumn<TypeOfCuisineItem, String> getTypeOfCuisineNameColumn() {
        return typeOfCuisineNameColumn;
    }

    public TableColumn<TypeOfCuisineItem, CheckBox> getTypeOfCuisineSelectColumn() {
        return typeOfCuisineSelectColumn;
    }

    public TableView<TypeOfCuisineItem> getTableViewTypeOfCuisine() {
        return tableViewTypeOfCuisine;
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