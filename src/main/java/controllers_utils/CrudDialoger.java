package controllers_utils;

import controllers.Controller;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Optional;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public abstract class CrudDialoger {
    public static void showAlertDialog(Controller controller, String alertMessage) {
        Stage stage = controller.getStage();
        Alert.AlertType alertType = Alert.AlertType.INFORMATION;
        Alert alert = new Alert(alertType, "");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(stage);
        alert.setTitle("Attenzione");
        alert.getDialogPane().setHeaderText(alertMessage);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == ButtonType.OK) {
                alert.close();
            }
        }
    }

    public static boolean areYouSureToDelete(Controller controller, String accomodationIdentifier) {
        Stage stage = controller.getStage();
        Alert.AlertType alertType = Alert.AlertType.CONFIRMATION;
        Alert alert = new Alert(alertType, "");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(stage);
        alert.setTitle("Attenzione");
        alert.getDialogPane().setHeaderText("Sei sicuro di voler eliminare " + accomodationIdentifier + " ?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == ButtonType.OK) {
                alert.close();
                return true;
            }
        }
        return false;
    }
}
