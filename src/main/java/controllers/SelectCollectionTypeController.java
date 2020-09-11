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
public class SelectCollectionTypeController extends Controller {
    private final SelectCollectionTypeView selectCollectionTypeView;

    public SelectCollectionTypeController(SelectCollectionTypeView selectCollectionTypeView) {
        this.selectCollectionTypeView = selectCollectionTypeView;
    }

    @Override
    public Stage getStage() {
        return (Stage) this.selectCollectionTypeView.getRootPane().getScene().getWindow();
    }

    public void setListenerOnButton(Button buttonVai) {
        if (buttonVai.getId().equals("buttonVai"))
            buttonVaiClicked();
    }

    public void setListenerOnComboBox(ComboBox<String> comboBox) {
        if (comboBox.getId().equals("comboBox"))
            comboBoxChanged();
    }

    public void comboBoxChanged() {
        selectCollectionTypeView.getComboBox().setOnAction(event -> selectCollectionTypeView.getButtonVai().setDisable(false));
    }

    public void buttonVaiClicked() {
        selectCollectionTypeView.getButtonVai().setOnAction(event -> {
            switch (selectCollectionTypeView.getComboBox().getValue()) {
                case "Ristoranti":
                    showCrudPageRestaurantStage();
                    break;
                case "Hotel":
                    showCrudPageHotelStage();
                    break;
                case "Attrazioni":
                    showCrudPageAttractionStage();
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
            stage.setTitle("CRUD Ristoranti");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showCrudPageHotelStage() {
        try {
            closeCurrentStage();
            Parent parent = FXMLLoader.load(getClass().getResource("/crud_hotel.fxml"));
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
            stage.setTitle("CRUD Hotel");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showCrudPageAttractionStage() {
        try {
            closeCurrentStage();
            Parent parent = FXMLLoader.load(getClass().getResource("/crud_attraction.fxml"));
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
            stage.setTitle("CRUD Attrazioni");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeCurrentStage() {
        getStage().close();
    }

    public void initializeView() {
        ObservableList<String> observableListOfAccomodations = FXCollections.observableArrayList("Attrazioni", "Hotel", "Ristoranti");
        selectCollectionTypeView.getComboBox().setItems(observableListOfAccomodations);
        selectCollectionTypeView.getButtonVai().setDisable(true);
    }
}
