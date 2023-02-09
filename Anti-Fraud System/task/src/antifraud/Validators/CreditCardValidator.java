package antifraud.Validators;

import org.apache.commons.validator.ValidatorException;
import org.apache.commons.validator.routines.checkdigit.LuhnCheckDigit;
import org.hibernate.validator.internal.constraintvalidators.hv.LuhnCheckValidator;

public class CreditCardValidator {

    private static final LuhnCheckDigit validator = new LuhnCheckDigit();

    public static void isValid(final String number) throws ValidatorException {
        if(!validator.isValid(number)) {
            throw new ValidatorException();
        }
    }
}
