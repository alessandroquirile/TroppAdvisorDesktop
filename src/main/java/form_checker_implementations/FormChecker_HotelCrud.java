package form_checker_implementations;

import controllers.Controller;
import controllers.CrudHotelController;
import form_checker_interfaces.FormChecker;

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
                crudHotelController.getCity().isEmpty() ||
                crudHotelController.getPrezzoMedio().isEmpty() ||
                crudHotelController.getNumeroDiTelefono().isEmpty() ||
                crudHotelController.getTipoIndirizzo() == null;
        //Bindings.isEmpty(crudHotelController.getImagesFromListView()).get();
    }
}
