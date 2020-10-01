package form_checker_implementations;

import controllers.Controller;
import controllers.CrudRestaurantController;
import controllers_utils.TypeOfCuisineItem;
import form_checker_interfaces.FormChecker;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public class FormChecker_RestaurantCrud implements FormChecker {
    public boolean formHasSomeEmptyField(Controller controller) {
        CrudRestaurantController crudRestaurantController = (CrudRestaurantController) controller;
        return crudRestaurantController.getNome().isEmpty() ||
                crudRestaurantController.getStrada().isEmpty() ||
                crudRestaurantController.getNumeroCivico().isEmpty() ||
                crudRestaurantController.getProvincia().isEmpty() ||
                crudRestaurantController.getCAP().isEmpty() ||
                crudRestaurantController.getCity().isEmpty() ||
                crudRestaurantController.getPrezzoMedio().isEmpty() ||
                crudRestaurantController.getNumeroDiTelefono().isEmpty() ||
                crudRestaurantController.getOpeningTime().isEmpty() ||
                crudRestaurantController.getTipoIndirizzo() == null ||
                Bindings.isEmpty(crudRestaurantController.getImagesFromListView()).get() ||
                !hasAtLeastOneTypeOfCuisineSelected(crudRestaurantController.getTypesOfCuisine());
    }

    private boolean hasAtLeastOneTypeOfCuisineSelected(ObservableList<TypeOfCuisineItem> typeOfCuisineItems) {
        for (TypeOfCuisineItem typeOfCuisineItem : typeOfCuisineItems) {
            if (typeOfCuisineItem.getCheckBox().isSelected())
                return true;
        }
        return false;
    }
}
