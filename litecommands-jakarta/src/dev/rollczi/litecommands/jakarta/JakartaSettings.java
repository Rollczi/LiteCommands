package dev.rollczi.litecommands.jakarta;

import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.message.InvokedMessage;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

public class JakartaSettings<SENDER> {

    Function<Invocation<SENDER>, Locale> localeProvider = ignored -> Locale.getDefault();
    ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    InvokedMessage<SENDER, Object, JakartaParsedResult> constraintViolationsMessage =
        (invocation, parsedResult) -> String.format("Constraint violations: %n%s", parsedResult.asJoinedString());

    private InvokedMessage<SENDER, String, JakartaViolation<?>> defaultViolationMessage =
        (invocation, violation) -> String.format("%s - %s", violation.getFormattedParameterName(), violation.getMessage());

    private Map<Class<? extends Annotation>, InvokedMessage<SENDER, String, ? extends JakartaViolation<?>>> violationMessages = new HashMap<>();

    public JakartaSettings<SENDER> locale(Function<Invocation<SENDER>, Locale>  localeProvider) {
        this.localeProvider = localeProvider;
        return this;
    }

    public JakartaSettings<SENDER> validatorFactory(ValidatorFactory validatorFactory) {
        this.validatorFactory = validatorFactory;
        return this;
    }

    public JakartaSettings<SENDER> constraintViolationsMessage(InvokedMessage<SENDER, Object, JakartaParsedResult> message) {
        this.constraintViolationsMessage = message;
        return this;
    }

    public <A extends Annotation> JakartaSettings<SENDER> violationMessage(Class<A> annotationType, InvokedMessage<SENDER, String, JakartaViolation<A>> message) {
        this.violationMessages.put(annotationType, message);
        return this;
    }

    public JakartaSettings<SENDER> violationMessage(InvokedMessage<SENDER, String, JakartaViolation<?>> message) {
        this.defaultViolationMessage = message;
        return this;
    }

    <A extends Annotation> String toMessage(Invocation<SENDER> invocation, JakartaViolation<A> violation) {
        InvokedMessage<SENDER, String, JakartaViolation<A>> message = (InvokedMessage<SENDER, String, JakartaViolation<A>>) violationMessages.get(violation.getAnnotationType());

        return message != null
            ? message.get(invocation, violation)
            : defaultViolationMessage.get(invocation, violation);
    }

}
