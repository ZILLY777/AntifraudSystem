package antifraud.Validators;

import org.apache.commons.validator.ValidatorException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Date;
import java.util.Locale;

public class DateValidator {

    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss",
            Locale.ROOT);
    public LocalTime afterDate;

    public static void isValid(String date) throws ParseException, ValidatorException {
        try {
            LocalDate.parse(date,dtf);
        } catch (DateTimeParseException e) {
            System.out.println(e);
            throw new ValidatorException("date");
        }

    }
}
