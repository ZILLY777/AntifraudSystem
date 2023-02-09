package antifraud.Validators;

import org.apache.commons.validator.ValidatorException;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.hibernate.exception.DataException;

public class IPv4ValidatorApache {
    private static final InetAddressValidator validator
            = InetAddressValidator.getInstance();
    private static final String PATTERN =
            "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";

    public static void isValid(final String ip) throws ValidatorException {
        try{if (!ip.matches(PATTERN)){
            throw new ValidatorException();
        }} catch (Exception e) {
            throw new ValidatorException("ip");
        }
    }
}
