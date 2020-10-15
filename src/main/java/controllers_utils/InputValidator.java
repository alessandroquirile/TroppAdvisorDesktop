package controllers_utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public abstract class InputValidator {
    public static boolean isValidTelephoneNumber(String telephoneNumber) {
        String telephoneNumberRegExp = "^([0-9]*-? ?/?[0-9]*)$";
        if (telephoneNumber.length() != 10 && telephoneNumber.length() != 9)
            return false;
        Pattern telephoneNumberPattern = Pattern.compile(telephoneNumberRegExp, Pattern.CASE_INSENSITIVE);
        Matcher matcher = telephoneNumberPattern.matcher(telephoneNumber);
        return matcher.find();
    }

    public static boolean isNumberGreaterOrEqualToZero(String s) {
        return isNumeric(s) && Double.parseDouble(s) >= 0;
    }

    /**
     * Metodo testato con JUnit4.
     * Verifica se la stringa passata in input rappresenta un numero (reale)
     *
     * @param s la stringa
     * @return vero se la stringa è un numero (reale); false altrimenti
     */
    public static boolean isNumeric(String s) {
        if (s == null)
            throw new IllegalArgumentException("Null arg");
        try {
            Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static boolean isIntegerGreaterOrEqualToZero(String s) {
        String numberRegExp = "^[0-9]+$";
        Pattern numberGreaterOrEqualToZeroPattern = Pattern.compile(numberRegExp, Pattern.CASE_INSENSITIVE);
        Matcher matcher = numberGreaterOrEqualToZeroPattern.matcher(s);
        return matcher.find();
    }

    public static boolean hasValidPattern(String email) {
        String emailRegExp = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern emailPattern = Pattern.compile(emailRegExp, Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailPattern.matcher(email);
        return matcher.find();
    }

    public static boolean isValidOpeningTime(String openingTime) {
        return isValidSingleOpeningTime(openingTime) || isValidTwoOpeningTime(openingTime);
    }

    // hh:mm - hh:mm
    private static boolean isValidSingleOpeningTime(String openingTime) {
        String regexp = "^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9] - (0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$";
        Pattern pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(openingTime);
        return matcher.find();
    }

    // hh:mm - hh:mm hh:mm - hh:mm
    private static boolean isValidTwoOpeningTime(String openingTime) {
        String regexp = "^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9] - (0[0-9]|1[0-9]|2[0-3]):[0-5][0-9] (0[0-9]|1[0-9]|2[0-3]):[0-5][0-9] - (0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$";
        Pattern pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(openingTime);
        return matcher.find();
    }
}
