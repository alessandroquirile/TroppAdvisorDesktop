package form_checker_implementations;

import controllers.Controller;
import controllers.CrudAttractionController;
import form_checker_interfaces.FormChecker;
import javafx.beans.binding.Bindings;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public class FormChecker_AttractionCrud implements FormChecker {
    public boolean formHasSomeEmptyField(Controller controller) {
        CrudAttractionController crudAttractionController = (CrudAttractionController) controller;
        return crudAttractionController.getNome().isEmpty() ||
                crudAttractionController.getStrada().isEmpty() ||
                crudAttractionController.getNumeroCivico().isEmpty() ||
                crudAttractionController.getProvincia().isEmpty() ||
                crudAttractionController.getCAP().isEmpty() ||
                crudAttractionController.getCity().isEmpty() ||
                crudAttractionController.getPrezzoMedio().isEmpty() ||
                /*crudAttractionController.getNumeroDiTelefono().isEmpty() ||*/
                crudAttractionController.getTipoIndirizzo() == null ||
                crudAttractionController.getOpeningTime().isEmpty() ||
                Bindings.isEmpty(crudAttractionController.getImagesFromListView()).get();

    }
}
