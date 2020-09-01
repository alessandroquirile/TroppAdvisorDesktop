package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public class SelectTypeController implements Initializable {

    @FXML
    private Button buttonVai;
    @FXML
    private ComboBox<String> comboBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> observableList = FXCollections.observableArrayList("Attrazioni", "Hotel", "Ristoranti");
        comboBox.setItems(observableList);
        buttonVai.setDisable(true);
    }

    @FXML
    public void comboChanged() {
        buttonVai.setDisable(false);
    }

    @FXML
    public void showSelection() {
        System.out.println(comboBox.getValue());
    }
}
