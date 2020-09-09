package form_checker_implementations;

import controllers.Controller;
import controllers.LoginController;
import form_checker_interfaces.FormChecker;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public class FormChecker_Login implements FormChecker {
    @Override
    public boolean formHasSomeEmptyField(Controller controller) {
        LoginController loginController = (LoginController) controller;
        return isEmpty(loginController.getEmail()) || isEmpty(loginController.getPassword());
    }

    private boolean isEmpty(String textField) {
        return textField.equals("");
    }
}