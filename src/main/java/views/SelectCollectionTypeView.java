package views;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public class SelectCollectionTypeView implements Initializable {

    @FXML
    private Button buttonVai;
    @FXML
    private ComboBox<String> comboBox;
    @FXML
    private AnchorPane rootPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controllers.SelectCollectionTypeController selectCollectionTypeController = new controllers.SelectCollectionTypeController(this);
        selectCollectionTypeController.initializeView();
        selectCollectionTypeController.setListenerOnButton(buttonVai);
        selectCollectionTypeController.setListenerOnComboBox(comboBox);
    }

    public Button getButtonVai() {
        return buttonVai;
    }

    public ComboBox<String> getComboBox() {
        return comboBox;
    }

    public AnchorPane getRootPane() {
        return rootPane;
    }
}