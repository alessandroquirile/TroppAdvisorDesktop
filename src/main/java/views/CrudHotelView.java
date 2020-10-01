package views;

import controllers.CrudHotelController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
        crudHotelController.setListenerOn(buttonInserisci, this);
        crudHotelController.setListenerOn(buttonCerca, this);
        crudHotelController.setListenerOn(buttonElimina, this);
        crudHotelController.setListenerOn(buttonModifica, this);
        crudHotelController.setListenerOn(buttonAnnulla, this);
        crudHotelController.setListenerOn(buttonIndietro, this);
        crudHotelController.setListenerOn(buttonConferma, this);
        crudHotelController.setListenerOn(buttonAiuto, this);
        crudHotelController.setListenerOn(buttonCaricaFoto, this);
        crudHotelController.setListenerOn(buttonMostraAvanti, this);
        crudHotelController.setListenerOn(buttonMostraIndietro, this);
        crudHotelController.setListenerOn(buttonEliminaFotoSelezionate, this);
        crudHotelController.setListenerOnTableView(tableView);
        crudHotelController.setListenerOnListView(listViewFotoPath, this);
        crudHotelController.setViewsAsDefault(this);
    }

    public Button getButtonCerca() {
        return buttonCerca;
    }

    public ChoiceBox<Integer> getChoiceBoxNumeroStelle() {
        return choiceBoxNumeroStelle;
    }

    public TableColumn<Object, Integer> getTableColumnNumeroStelle() {
        return tableColumnNumeroStelle;
    }
}
