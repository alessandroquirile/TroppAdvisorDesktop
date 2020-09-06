package controllers_utils;

import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public class UserInputChecker {
    public static boolean isValidTelephoneNumber(String telephoneNumber) {
        String telephoneNumberRegExp = "^([0-9]*\\-?\\ ?\\/?[0-9]*)$";
        if (telephoneNumber.length() == 10 || telephoneNumber.length() == 9) {
            Pattern telephoneNumberPattern = Pattern.compile(telephoneNumberRegExp, Pattern.CASE_INSENSITIVE);
            Matcher matcher = telephoneNumberPattern.matcher(telephoneNumber);
            return matcher.find();
        } else {
            return false;
        }
    }

    public static boolean isValidNumberGreaterOrEqualToZero(String number) {
        String numberRegExp = "^[0-9]+$";
        Pattern numberGreaterOrEqualToZeroPattern = Pattern.compile(numberRegExp, Pattern.CASE_INSENSITIVE);
        Matcher matcher = numberGreaterOrEqualToZeroPattern.matcher(number);
        return matcher.find();
    }

    public static boolean isValidOpeningTimeAtMorning(String orarioApertura, String orarioChiusura) {
        return LocalTime.parse(orarioApertura).isBefore(LocalTime.parse(orarioChiusura));
    }

    public static boolean isValidOpeningTimeAtEvening(String orarioApertura, String orarioChiusura) {
        // todo...
        return true;
    }
}
