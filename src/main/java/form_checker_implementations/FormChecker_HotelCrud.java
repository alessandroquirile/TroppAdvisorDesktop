package form_checker_implementations;

import controllers.Controller;
import controllers.CrudHotelController;
import form_checker_interfaces.FormChecker;
import javafx.beans.binding.Bindings;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public class FormChecker_HotelCrud implements FormChecker {
    public boolean formHasSomeEmptyField(Controller controller) {
        CrudHotelController crudHotelController = (CrudHotelController) controller;
        return crudHotelController.getNome().isEmpty() ||
                crudHotelController.getStrada().isEmpty() ||
                crudHotelController.getNumeroCivico().isEmpty() ||
                crudHotelController.getProvincia().isEmpty() ||
                crudHotelController.getCAP().isEmpty() ||
                crudHotelController.getCittà().isEmpty() ||
                crudHotelController.getPrezzoMedio().isEmpty() ||
                crudHotelController.getNumeroDiTelefono().isEmpty() ||
                Bindings.isEmpty(crudHotelController.getImagesFromListView()).get();
    }
}
