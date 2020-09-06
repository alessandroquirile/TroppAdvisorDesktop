package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import views.SelectCollectionTypeView;

import java.io.IOException;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public class SelectCollectionTypeController {
    private final SelectCollectionTypeView selectCollectionTypeView;

    public SelectCollectionTypeController(SelectCollectionTypeView selectCollectionTypeView) {
        this.selectCollectionTypeView = selectCollectionTypeView;
    }

    public void setListenerOnButton(Button buttonVai) {
        if (selectCollectionTypeView.getButtonVai().getId().equals("buttonVai"))
            buttonVaiClicked();
    }

    public void setListenerOnComboBox(ComboBox<String> comboBox) {
        if (selectCollectionTypeView.getComboBox().getId().equals("comboBox"))
            comboBoxChanged();
    }

    public void comboBoxChanged() {
        selectCollectionTypeView.getComboBox().setOnAction(event -> {
            selectCollectionTypeView.getButtonVai().setDisable(false);
        });
    }

    public void buttonVaiClicked() {
        selectCollectionTypeView.getButtonVai().setOnAction(event -> {
            switch (selectCollectionTypeView.getComboBox().getValue()) {
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
        });
    }

    public void showCrudPageRestaurantStage() {
        try {
            closeCurrentStage();
            Parent parent = FXMLLoader.load(getClass().getResource("/crud_restaurant.fxml"));
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
            stage.setTitle("CRUD Ristorante");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeCurrentStage() {
        Stage thisStage = (Stage) selectCollectionTypeView.getRootPane().getScene().getWindow();
        thisStage.close();
    }

    public void initializeView() {
        ObservableList<String> observableListOfAccomodations = FXCollections.observableArrayList("Attrazioni", "Hotel", "Ristoranti");
        selectCollectionTypeView.getComboBox().setItems(observableListOfAccomodations);
        selectCollectionTypeView.getButtonVai().setDisable(true);
    }
}
