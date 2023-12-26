package dev.rollczi.litecommands.jakarta;

import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.message.MessageKey;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import java.util.Locale;
import java.util.function.Function;

public class JakartaSettings<SENDER> {

    Function<Invocation<SENDER>, Locale> localeProvider = ignored -> Locale.getDefault();
    ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    String constraintViolationDelimiter = " - ";
    MessageKey<String> constrainsViolationMessage = MessageKey.of("constraint-violations",
        input -> String.format("&cConstraint violations: %n%s (CONSTRAINT_VIOLATIONS)", input));

    public JakartaSettings<SENDER> locale(Function<Invocation<SENDER>, Locale>  localeProvider) {
        this.localeProvider = localeProvider;
        return this;
    }

    public JakartaSettings<SENDER> validatorFactory(ValidatorFactory validatorFactory) {
        this.validatorFactory = validatorFactory;
        return this;
    }

    public JakartaSettings<SENDER> constraintViolationDelimiter(String constraintViolationDelimiter) {
        this.constraintViolationDelimiter = constraintViolationDelimiter;
        return this;
    }

    public JakartaSettings<SENDER> constrainsViolationMessage(Function<String, Object> defaultMessage) {
        this.constrainsViolationMessage = MessageKey.of("constraint-violations", defaultMessage);
        return this;
    }

}
