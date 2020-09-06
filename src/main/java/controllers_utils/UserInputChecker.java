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
        /*
        System.out.println(isValidOpeningTimeAtEvening("15:00", "15:00"));
		System.out.println(isValidOpeningTimeAtEvening("15:00", "15:30"));
		System.out.println(isValidOpeningTimeAtEvening("16:00", "15:00"));
		System.out.println(isValidOpeningTimeAtEvening("15:00", "03:00"));
		System.out.println(isValidOpeningTimeAtEvening("02:00", "03:00"));
		System.out.println(isValidOpeningTimeAtEvening("03:00", "02:00"));
		System.out.println(isValidOpeningTimeAtEvening("03:00", "03:00"));
		System.out.println(isValidOpeningTimeAtEvening("03:00", "03:30"));
         */
        int hApertura = Integer.parseInt(orarioApertura.substring(0, 2));
        int hChiusura = Integer.parseInt(orarioChiusura.substring(0, 2));
        if (hApertura >= 15 && hApertura <= 23) {
            if (hChiusura >= 15 && hChiusura <= 23) {
                return isValidOpeningTimeAtMorning(orarioApertura, orarioChiusura);
            } else {
                // Tra 00 e le 03
                return !isValidOpeningTimeAtMorning(orarioApertura, orarioChiusura);
            }
        } else {
            return isValidOpeningTimeAtMorning(orarioApertura, orarioChiusura);
        }
    }
}
