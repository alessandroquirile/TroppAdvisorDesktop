package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public class CrudRestaurantController {
    public javafx.scene.control.Button buttonElimina;
    public javafx.scene.control.Button buttonConferma;
    public javafx.scene.control.Button buttonAnnulla;
    public javafx.scene.control.Button buttonAiuto;
    public TableView tableView;
    public javafx.scene.control.Button buttonModifica;
    public javafx.scene.control.TextField textFieldNome;
    public javafx.scene.control.TextField textFieldDescrizione;
    public javafx.scene.control.TextField textFieldIndirizzo;
    public ChoiceBox choiceBoxIndirizzo;
    public javafx.scene.control.TextField textFieldCittà;
    public ChoiceBox choiceBoxRangePrezzo;
    public CheckBox checkBoxCertificatoDiEccellenza;
    public ChoiceBox choiceBoxTipoDiCucina;
    public javafx.scene.control.Button buttonIndietro;
    public javafx.scene.control.Button buttonInserisci;
    public AnchorPane rootPane;
    public javafx.scene.control.TextField textFieldTipoDiCucina;
    public javafx.scene.control.TextField textFieldCap;

    @FXML
    public void showSelectTypeStage(MouseEvent mouseEvent) {
        try {
            closeCurrentStage();
            Parent parent = FXMLLoader.load(CrudRestaurantController.class.getResource("/select_type.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeCurrentStage() {
        Stage thisStage = (Stage) rootPane.getScene().getWindow();
        thisStage.close();
    }

    @FXML
    public void buttonInserisciClicked(MouseEvent mouseEvent) {
        enableAllTextFields();
        enableAllChoiceBox();
        disableCrudButtons();
        buttonConferma.setDisable(false);
        buttonAnnulla.setDisable(false);
        buttonIndietro.setDisable(true);
    }

    private void enableAllTextFields() {
        textFieldDescrizione.setDisable(false);
        textFieldIndirizzo.setDisable(false);
        textFieldCap.setDisable(false);
        textFieldCittà.setDisable(false);
        textFieldTipoDiCucina.setDisable(false);
        textFieldNome.setDisable(false);
    }

    private void enableAllChoiceBox() {
        choiceBoxRangePrezzo.setDisable(false);
        checkBoxCertificatoDiEccellenza.setDisable(false);
        choiceBoxTipoDiCucina.setDisable(false);
        choiceBoxIndirizzo.setDisable(false);
    }

    private void disableCrudButtons() {
        buttonInserisci.setDisable(true);
        buttonModifica.setDisable(true);
        buttonElimina.setDisable(true);
    }

    @FXML
    public void buttonAnnullaClicked(MouseEvent mouseEvent) {
        // TODO: mostrare popup di conferma. Se conferma...
        enableAndDisableAsDefault();
    }

    private void enableAndDisableAsDefault() {
        buttonIndietro.setDisable(false);
        buttonInserisci.setDisable(false);
        buttonModifica.setDisable(true);
        buttonElimina.setDisable(true);
        buttonConferma.setDisable(true);
        buttonAnnulla.setDisable(true);
        buttonAiuto.setDisable(false);
        textFieldNome.setDisable(true);
        textFieldDescrizione.setDisable(true);
        textFieldIndirizzo.setDisable(true);
        choiceBoxIndirizzo.setDisable(true);
        textFieldCap.setDisable(true);
        textFieldCittà.setDisable(true);
        choiceBoxRangePrezzo.setDisable(true);
        checkBoxCertificatoDiEccellenza.setDisable(true);
        choiceBoxTipoDiCucina.setDisable(true);
        textFieldTipoDiCucina.setDisable(true);
    }

    @FXML
    public void buttonConfermaClicked(MouseEvent mouseEvent) {
        System.out.println("Cliccato conferma"); //dbg
    }

    @FXML
    public void buttonAiutoClicked(MouseEvent mouseEvent) {
        System.out.println("Cliccato aiuto"); //dbg
    }
}