package views;

import controllers.CrudRestaurantController;
import controllers_utils.TypeOfCuisineItem;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public class CrudRestaurantView extends CrudView {
    @FXML
    private TextField textFieldOpeningTime;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CrudRestaurantController crudRestaurantController = new CrudRestaurantController(this);
        crudRestaurantController.setListenerOn(buttonInserisci, this);
        crudRestaurantController.setListenerOn(buttonCerca, this);
        crudRestaurantController.setListenerOn(buttonElimina, this);
        crudRestaurantController.setListenerOn(buttonModifica, this);
        crudRestaurantController.setListenerOn(buttonAnnulla, this);
        crudRestaurantController.setListenerOn(buttonIndietro, this);
        crudRestaurantController.setListenerOn(buttonConferma, this);
        crudRestaurantController.setListenerOn(buttonAiuto, this);
        crudRestaurantController.setListenerOn(buttonCaricaFoto, this);
        crudRestaurantController.setListenerOn(buttonMostraAvanti, this);
        crudRestaurantController.setListenerOn(buttonMostraIndietro, this);
        crudRestaurantController.setListenerOn(buttonEliminaFotoSelezionate, this);
        crudRestaurantController.setListenerOnTableView(tableView);
        crudRestaurantController.setListenerOnListView(listViewFotoPath, this);
        crudRestaurantController.setViewsAsDefault(this);
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

    public TextField getTextFieldOpeningTime() {
        return textFieldOpeningTime;
    }
}