package controllers_utils;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public class InputValidatorTest {

    // Black-Box testing

    // n is null
    @Test(expected = IllegalArgumentException.class)
    public void isNumericStringaNull() {
        InputValidator.isNumeric(null);
    }

    // n is ""
    @Test
    public void isNumericStringaVuota() {
        assertFalse(InputValidator.isNumeric(""));
    }

    // n in {0}
    @Test
    public void isNumericZero() {
        assertTrue(InputValidator.isNumeric("0"));
    }

    // n in N-{0}
    @Test
    public void isNumericNumeroNaturale() {
        assertTrue(InputValidator.isNumeric("3"));
    }

    // n in Z-N-{0}
    @Test
    public void isNumericNumeroIntero() {
        assertTrue(InputValidator.isNumeric("-3"));
    }

    // n in Q-Z-N-{0}
    @Test
    public void isNumericNumeroRazionale() {
        assertTrue(InputValidator.isNumeric("2.66"));
    }

    // n in R-Q-Z-N-{0}
    @Test
    public void isNumericNumeroReale() {
        assertTrue(InputValidator.isNumeric(String.valueOf(Math.sqrt(2))));
    }

    // n not in R
    @Test
    public void isNumericTestoNonVuoto() {
        assertFalse(InputValidator.isNumeric("hello"));
    }

    // White-Box testing, metodologia Branch Coverage

    @Test(expected = IllegalArgumentException.class)
    public void isNumericPath_30_31() {
        InputValidator.isNumeric(null);
    }

    @Test
    public void isNumericPath_30_33_35() {
        assertFalse(InputValidator.isNumeric("hello"));
    }

    @Test
    public void isNumericPath_30_33_37() {
        assertTrue(InputValidator.isNumeric("12"));
    }
}