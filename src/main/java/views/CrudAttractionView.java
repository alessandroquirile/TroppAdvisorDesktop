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
        crudAttractionController.setListenerOn(buttonInserisci, this);
        crudAttractionController.setListenerOn(buttonElimina, this);
        crudAttractionController.setListenerOn(buttonModifica, this);
        crudAttractionController.setListenerOn(buttonAnnulla, this);
        crudAttractionController.setListenerOn(buttonIndietro, this);
        crudAttractionController.setListenerOn(buttonConferma, this);
        crudAttractionController.setListenerOn(buttonAiuto, this);
        crudAttractionController.setListenerOn(buttonCaricaFoto, this);
        crudAttractionController.setListenerOn(buttonMostraAvanti, this);
        crudAttractionController.setListenerOn(buttonMostraIndietro, this);
        crudAttractionController.setListenerOn(buttonCerca, this);
        crudAttractionController.setListenerOn(buttonEliminaFotoSelezionate, this);
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
