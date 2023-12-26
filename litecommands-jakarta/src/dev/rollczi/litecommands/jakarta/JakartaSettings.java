package dev.rollczi.litecommands.jakarta;


import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import java.util.Locale;

public class JakartaSettings {

    Locale locale = Locale.getDefault();
    ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    String constraintViolationDelimiter = " - ";

    public JakartaSettings locale(Locale locale) {
        this.locale = locale;
        return this;
    }

    public JakartaSettings validatorFactory(ValidatorFactory validatorFactory) {
        this.validatorFactory = validatorFactory;
        return this;
    }

    public JakartaSettings constraintViolationDelimiter(String constraintViolationDelimiter) {
        this.constraintViolationDelimiter = constraintViolationDelimiter;
        return this;
    }

}
