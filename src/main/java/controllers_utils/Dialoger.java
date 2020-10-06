package controllers_utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;

import java.util.Optional;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public abstract class Dialoger {
    public static void showAlertDialog(String alertMessage) {
        Alert.AlertType alertType = Alert.AlertType.INFORMATION;
        Alert alert = new Alert(alertType, "");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setTitle("Attenzione");
        alert.getDialogPane().setHeaderText(alertMessage);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == ButtonType.OK)
                alert.close();
        }
    }

    public static boolean areYouSureToDelete(String somethingToDelete) {
        ButtonType yes = new ButtonType("Sì", ButtonBar.ButtonData.YES);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.NO);
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "", yes, no);
        alert.setTitle("Attenzione");
        alert.setHeaderText("Sei sicuro di eliminare " + somethingToDelete + " ?");
        alert.initModality(Modality.APPLICATION_MODAL);
        Optional<ButtonType> result = alert.showAndWait();
        return result.orElse(no) == yes;
    }

    public static void showHelpDialog() {
        Alert.AlertType alertType = Alert.AlertType.INFORMATION;
        Alert alert = new Alert(alertType, "");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setTitle("Come usare l'interfaccia?");
        alert.getDialogPane().setHeaderText("Seleziona un'operazione CRUD in alto a sinistra " +
                "e poi conferma la tua scelta dopo aver inserito i dati nel form");
        alert.getDialogPane().setContentText("Per aggiornare o eliminare un record già esistente, selezionarlo dapprima dalla tabella");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == ButtonType.OK)
                alert.close();
        }
    }

    public static boolean ignoreExcellence() {
        ButtonType yes = new ButtonType("Ignora certificato di eccellenza", ButtonBar.ButtonData.YES);
        ButtonType no = new ButtonType("Cerca strutture senza certificato di eccellenza", ButtonBar.ButtonData.NO);
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "", yes, no);
        alert.setTitle("Attenzione");
        alert.setHeaderText("Non hai selezionato il Certificato di Eccellenza. Vuoi ignorarlo?");
        alert.initModality(Modality.APPLICATION_MODAL);
        Optional<ButtonType> result = alert.showAndWait();
        return result.orElse(no) == yes;
    }
}
