package views;

import controllers.CrudAttractionController;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public class CrudAttractionView extends CrudView {
    @FXML
    private TextField textFieldOpeningTime;
    @FXML
    private TableColumn<Object, String> tableColumnOrarioApertura;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CrudAttractionController crudAttractionController = new CrudAttractionController(this);
        crudAttractionController.setListenerOnButton(buttonInserisci, this);
        crudAttractionController.setListenerOnButton(buttonElimina, this);
        crudAttractionController.setListenerOnButton(buttonModifica, this);
        crudAttractionController.setListenerOnButton(buttonAnnulla, this);
        crudAttractionController.setListenerOnButton(buttonIndietro, this);
        crudAttractionController.setListenerOnButton(buttonConferma, this);
        crudAttractionController.setListenerOnButton(buttonAiuto, this);
        crudAttractionController.setListenerOnButton(buttonCaricaFoto, this);
        crudAttractionController.setListenerOnButton(buttonMostraAvanti, this);
        crudAttractionController.setListenerOnButton(buttonMostraIndietro, this);
        crudAttractionController.setListenerOnButton(buttonCerca, this);
        crudAttractionController.setListenerOnButton(buttonEliminaFotoSelezionate, this);
        crudAttractionController.setListenerOnTableView(tableView, this);
        crudAttractionController.setListenerOnListView(listViewFotoPath, this);
        crudAttractionController.setViewsAsDefault(this);
    }

    public TableColumn<Object, String> getTableColumnOrarioApertura() {
        return tableColumnOrarioApertura;
    }

    public TextField getTextFieldOpeningTime() {
        return textFieldOpeningTime;
    }
}
