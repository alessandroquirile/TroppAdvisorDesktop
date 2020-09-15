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
        crudAttractionController.setListenerOn(buttonInserisci);
        crudAttractionController.setListenerOn(buttonElimina);
        crudAttractionController.setListenerOn(buttonModifica);
        crudAttractionController.setListenerOn(buttonAnnulla);
        crudAttractionController.setListenerOn(buttonIndietro);
        crudAttractionController.setListenerOn(buttonConferma);
        crudAttractionController.setListenerOn(buttonAiuto);
        crudAttractionController.setListenerOn(buttonCaricaFoto);
        crudAttractionController.setListenerOn(buttonMostraAvanti);
        crudAttractionController.setListenerOn(buttonMostraIndietro);
        crudAttractionController.setListenerOn(buttonCerca);
        crudAttractionController.setListenerOn(buttonEliminaFotoSelezionate);
        crudAttractionController.setListenerOnTableView(tableView);
        crudAttractionController.setListenerOnListView(listViewFotoPath);
        crudAttractionController.setViewsAsDefault();
    }

    public TableColumn<Object, String> getTableColumnOrarioApertura() {
        return tableColumnOrarioApertura;
    }

    public TextField getTextFieldOpeningTime() {
        return textFieldOpeningTime;
    }
}
