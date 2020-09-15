package views;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public abstract class CrudView implements Initializable {
    @FXML
    protected Button buttonCerca;
    @FXML
    protected Button buttonMostraAvanti;
    @FXML
    protected Button buttonMostraIndietro;
    @FXML
    protected Button buttonEliminaFotoSelezionate;
    @FXML
    protected TextField textFieldPrezzoMedio;
    @FXML
    protected TextField txtFieldNumeroCivico;
    @FXML
    protected TextField textFieldProvincia;
    @FXML
    protected TextField textFieldStrada;
    @FXML
    protected TableColumn<Object, String> tableColumnPuntoSuMappa;
    @FXML
    protected TableColumn<Object, String> tableColumnImmagini;
    @FXML
    protected javafx.scene.control.Button buttonModifica;
    @FXML
    protected javafx.scene.control.TextField textFieldNome;
    @FXML
    protected ChoiceBox<String> choiceBoxIndirizzo;
    @FXML
    protected javafx.scene.control.TextField textFieldCity;
    @FXML
    protected CheckBox checkBoxCertificatoDiEccellenza;
    @FXML
    protected javafx.scene.control.Button buttonIndietro;
    @FXML
    protected javafx.scene.control.Button buttonInserisci;
    @FXML
    protected javafx.scene.control.TextField textFieldCAP;
    @FXML
    protected TextField textFieldNumeroDiTelefono;
    @FXML
    protected Button buttonCaricaFoto;
    @FXML
    protected ListView<String> listViewFotoPath;
    @FXML
    protected TableColumn<Object, Integer> tableColumnVotoMedio;
    @FXML
    protected TableColumn<Object, Integer> tableColumnPrezzoMedio;
    @FXML
    protected TableColumn<Object, String> tableColumnNumeroDiTelefono;
    @FXML
    protected TableColumn<Object, Boolean> tableColumnHasCertificateOfExcellence;
    @FXML
    protected TableColumn<Object, Integer> tableColumnTotalReview;
    @FXML
    protected TableColumn<Object, String> tableColumnAddedDate;
    @FXML
    protected TableColumn<Object, String> tableColumnLastModificationDate;
    @FXML
    protected TableColumn<Object, String> tableColumnIndirizzo;
    @FXML
    protected AnchorPane rootPane;
    @FXML
    protected javafx.scene.control.Button buttonElimina;
    @FXML
    protected javafx.scene.control.Button buttonConferma;
    @FXML
    protected javafx.scene.control.Button buttonAnnulla;
    @FXML
    protected javafx.scene.control.Button buttonAiuto;
    @FXML
    protected TableView<Object> tableView;
    @FXML
    protected TableColumn<Object, String> tableColumnName;
    @FXML
    protected TableColumn<Object, String> tableColumnId;

    public Button getButtonCerca() {
        return buttonCerca;
    }

    public Button getButtonMostraAvanti() {
        return buttonMostraAvanti;
    }

    public Button getButtonMostraIndietro() {
        return buttonMostraIndietro;
    }

    public Button getButtonEliminaFotoSelezionate() {
        return buttonEliminaFotoSelezionate;
    }

    public TextField getTextFieldPrezzoMedio() {
        return textFieldPrezzoMedio;
    }

    public TextField getTxtFieldNumeroCivico() {
        return txtFieldNumeroCivico;
    }

    public TextField getTextFieldProvincia() {
        return textFieldProvincia;
    }

    public TextField getTextFieldStrada() {
        return textFieldStrada;
    }

    public TableColumn<Object, String> getTableColumnPuntoSuMappa() {
        return tableColumnPuntoSuMappa;
    }

    public TableColumn<Object, String> getTableColumnImmagini() {
        return tableColumnImmagini;
    }

    public Button getButtonModifica() {
        return buttonModifica;
    }

    public TextField getTextFieldNome() {
        return textFieldNome;
    }

    public ChoiceBox<String> getChoiceBoxIndirizzo() {
        return choiceBoxIndirizzo;
    }

    public TextField getTextFieldCity() {
        return textFieldCity;
    }

    public CheckBox getCheckBoxCertificatoDiEccellenza() {
        return checkBoxCertificatoDiEccellenza;
    }

    public Button getButtonIndietro() {
        return buttonIndietro;
    }

    public Button getButtonInserisci() {
        return buttonInserisci;
    }

    public TextField getTextFieldCAP() {
        return textFieldCAP;
    }

    public TextField getTextFieldNumeroDiTelefono() {
        return textFieldNumeroDiTelefono;
    }

    public Button getButtonCaricaFoto() {
        return buttonCaricaFoto;
    }

    public ListView<String> getListViewFotoPath() {
        return listViewFotoPath;
    }

    public TableColumn<Object, Integer> getTableColumnVotoMedio() {
        return tableColumnVotoMedio;
    }

    public TableColumn<Object, Integer> getTableColumnPrezzoMedio() {
        return tableColumnPrezzoMedio;
    }

    public TableColumn<Object, String> getTableColumnNumeroDiTelefono() {
        return tableColumnNumeroDiTelefono;
    }

    public TableColumn<Object, Boolean> getTableColumnHasCertificateOfExcellence() {
        return tableColumnHasCertificateOfExcellence;
    }

    public TableColumn<Object, Integer> getTableColumnTotalReview() {
        return tableColumnTotalReview;
    }

    public TableColumn<Object, String> getTableColumnAddedDate() {
        return tableColumnAddedDate;
    }

    public TableColumn<Object, String> getTableColumnLastModificationDate() {
        return tableColumnLastModificationDate;
    }

    public TableColumn<Object, String> getTableColumnIndirizzo() {
        return tableColumnIndirizzo;
    }

    public AnchorPane getRootPane() {
        return rootPane;
    }

    public Button getButtonElimina() {
        return buttonElimina;
    }

    public Button getButtonConferma() {
        return buttonConferma;
    }

    public Button getButtonAnnulla() {
        return buttonAnnulla;
    }

    public Button getButtonAiuto() {
        return buttonAiuto;
    }

    public TableView<Object> getTableView() {
        return tableView;
    }

    public TableColumn<Object, String> getTableColumnName() {
        return tableColumnName;
    }

    public TableColumn<Object, String> getTableColumnId() {
        return tableColumnId;
    }
}
