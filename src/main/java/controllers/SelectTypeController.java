package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
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
    @FXML
    private AnchorPane rootPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> observableListOfAccomodations = FXCollections.observableArrayList("Attrazioni", "Hotel", "Ristoranti");
        comboBox.setItems(observableListOfAccomodations);
        buttonVai.setDisable(true);
    }

    @FXML
    public void comboChanged() {
        buttonVai.setDisable(false);
    }

    @FXML
    public void showCrudPage() {
        switch (comboBox.getValue()) {
            case "Ristoranti":
                showCrudPageRestaurantStage();
                break;
            case "Hotel":
                System.out.println("Hotel");
                break;
            case "Attrazioni":
                System.out.println("Attrazioni");
                break;
        }
    }

    public void showCrudPageRestaurantStage() {
        try {
            Parent parent = FXMLLoader.load(SelectTypeController.class.getResource("/crud_restaurant.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(parent);
            Screen screen = Screen.getPrimary();
            Rectangle2D rectangle2D = screen.getVisualBounds();
            stage.setX(rectangle2D.getMinX());
            stage.setY(rectangle2D.getMinX());
            stage.setWidth(rectangle2D.getWidth());
            stage.setHeight(rectangle2D.getHeight());
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
            closeCurrentStage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeCurrentStage() {
        Stage thisStage = (Stage) rootPane.getScene().getWindow();
        thisStage.close();
    }
}