package views;

import controllers.CrudHotelController;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public class CrudHotelView extends CrudView {
    @FXML
    private TableColumn<Object, Integer> tableColumnNumeroStelle;
    @FXML
    private ChoiceBox<Integer> choiceBoxNumeroStelle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CrudHotelController crudHotelController = new CrudHotelController(this);
        crudHotelController.setListenerOnButton(buttonInserisci, this);
        crudHotelController.setListenerOnButton(buttonCerca, this);
        crudHotelController.setListenerOnButton(buttonElimina, this);
        crudHotelController.setListenerOnButton(buttonModifica, this);
        crudHotelController.setListenerOnButton(buttonAnnulla, this);
        crudHotelController.setListenerOnButton(buttonIndietro, this);
        crudHotelController.setListenerOnButton(buttonConferma, this);
        crudHotelController.setListenerOnButton(buttonAiuto, this);
        crudHotelController.setListenerOnButton(buttonCaricaFoto, this);
        crudHotelController.setListenerOnButton(buttonMostraAvanti, this);
        crudHotelController.setListenerOnButton(buttonMostraIndietro, this);
        crudHotelController.setListenerOnButton(buttonEliminaFotoSelezionate, this);
        crudHotelController.setListenerOnTableView(tableView, this);
        crudHotelController.setListenerOnListView(listViewFotoPath, this);
        crudHotelController.setViewsAsDefault(this);
    }

    public ChoiceBox<Integer> getChoiceBoxNumeroStelle() {
        return choiceBoxNumeroStelle;
    }

    public TableColumn<Object, Integer> getTableColumnNumeroStelle() {
        return tableColumnNumeroStelle;
    }
}
