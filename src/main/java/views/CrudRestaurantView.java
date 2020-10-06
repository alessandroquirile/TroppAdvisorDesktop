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
        crudRestaurantController.setListenerOnButton(buttonInserisci, this);
        crudRestaurantController.setListenerOnButton(buttonCerca, this);
        crudRestaurantController.setListenerOnButton(buttonElimina, this);
        crudRestaurantController.setListenerOnButton(buttonModifica, this);
        crudRestaurantController.setListenerOnButton(buttonAnnulla, this);
        crudRestaurantController.setListenerOnButton(buttonIndietro, this);
        crudRestaurantController.setListenerOnButton(buttonConferma, this);
        crudRestaurantController.setListenerOnButton(buttonAiuto, this);
        crudRestaurantController.setListenerOnButton(buttonCaricaFoto, this);
        crudRestaurantController.setListenerOnButton(buttonMostraAvanti, this);
        crudRestaurantController.setListenerOnButton(buttonMostraIndietro, this);
        crudRestaurantController.setListenerOnButton(buttonEliminaFotoSelezionate, this);
        crudRestaurantController.setListenerOnTableView(tableView, this);
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