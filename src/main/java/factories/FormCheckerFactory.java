package factories;

import controllers.*;
import form_checker_implementations.FormChecker_AttractionCrud;
import form_checker_implementations.FormChecker_HotelCrud;
import form_checker_implementations.FormChecker_Login;
import form_checker_implementations.FormChecker_RestaurantCrud;
import form_checker_interfaces.FormChecker;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public class FormCheckerFactory {
    private static FormCheckerFactory formCheckerFactorySingletonInstance = null;

    private FormCheckerFactory() {

    }

    public static synchronized FormCheckerFactory getInstance() {
        if (formCheckerFactorySingletonInstance == null)
            formCheckerFactorySingletonInstance = new FormCheckerFactory();
        return formCheckerFactorySingletonInstance;
    }

    public FormChecker getFormChecker(Controller controller) {
        if (controller instanceof LoginController)
            return new FormChecker_Login();
        else if (controller instanceof CrudRestaurantController)
            return new FormChecker_RestaurantCrud();
        else if (controller instanceof CrudHotelController)
            return new FormChecker_HotelCrud();
        else if (controller instanceof CrudAttractionController)
            return new FormChecker_AttractionCrud();
        else
            throw new RuntimeException(controller.toString() + " inesistente");
    }
}