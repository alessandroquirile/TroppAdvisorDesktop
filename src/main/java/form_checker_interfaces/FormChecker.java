package form_checker_interfaces;

import controllers.Controller;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public interface FormChecker {
    boolean formHasSomeEmptyField(Controller controller);
}