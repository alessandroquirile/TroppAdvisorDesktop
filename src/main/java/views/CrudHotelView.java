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
        crudHotelController.setListenerOn(buttonInserisci);
        crudHotelController.setListenerOn(buttonCerca);
        crudHotelController.setListenerOn(buttonElimina);
        crudHotelController.setListenerOn(buttonModifica);
        crudHotelController.setListenerOn(buttonAnnulla);
        crudHotelController.setListenerOn(buttonIndietro);
        crudHotelController.setListenerOn(buttonConferma);
        crudHotelController.setListenerOn(buttonAiuto);
        crudHotelController.setListenerOn(buttonCaricaFoto);
        crudHotelController.setListenerOn(buttonMostraAvanti);
        crudHotelController.setListenerOn(buttonMostraIndietro);
        crudHotelController.setListenerOn(buttonEliminaFotoSelezionate);
        crudHotelController.setListenerOnTableView(tableView);
        crudHotelController.setListenerOnListView(listViewFotoPath);
        crudHotelController.setViewsAsDefault();
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
