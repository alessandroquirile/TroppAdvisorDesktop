package views;

import controllers.CrudAttractionController;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public class CrudAttractionView extends FormView {
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
        crudAttractionController.setListenerOn(buttonEliminaFotoSelezionate);
        crudAttractionController.setListenerOnTableView(tableView);
        crudAttractionController.setListenerOnListView(listViewFotoPath);
        crudAttractionController.setViewsAsDefault();
    }

    public TableColumn<Object, String> getTableColumnOrarioApertura() {
        return tableColumnOrarioApertura;
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
