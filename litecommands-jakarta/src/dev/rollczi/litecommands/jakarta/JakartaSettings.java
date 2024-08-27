package dev.rollczi.litecommands.jakarta;

import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.message.InvokedMessage;
import dev.rollczi.litecommands.shared.BiHashMap;
import dev.rollczi.litecommands.shared.BiMap;
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

    private InvokedMessage<SENDER, ?, JakartaViolation<?, ?>> defaultViolationMessage =
        (invocation, violation) -> String.format("%s - %s", violation.getFormattedParameterName(), violation.getMessage());

    private BiMap<Class<? extends Annotation>, Class<?>, InvokedMessage<SENDER, ?, ? extends JakartaViolation<?, ?>>> violationMessages = new BiHashMap<>();

    public JakartaSettings<SENDER> locale(Function<Invocation<SENDER>, Locale>  localeProvider) {
        this.localeProvider = localeProvider;
        return this;
    }

    public JakartaSettings<SENDER> validatorFactory(ValidatorFactory validatorFactory) {
        this.validatorFactory = validatorFactory;
        return this;
    }

    /**
     * It is not recommended to use this method. Use {@link #violationMessage(Class, InvokedMessage)} instead.
     * If you want to join all violations into one message, then implement your ResultHandler for JakartaRawResult type.
     */
    @Deprecated
    public JakartaSettings<SENDER> constraintViolationsMessage(InvokedMessage<SENDER, Object, JakartaParsedResult> message) {
        throw new UnsupportedOperationException("Use violationMessage(Class, InvokedMessage) instead. If you want to join all violations into one message, then implement your handler: ResultHandler<SENDER, JakartaRawResult>.");
    }

    public <A extends Annotation> JakartaSettings<SENDER> violationMessage(Class<A> annotationType, InvokedMessage<SENDER, ?, JakartaViolation<A, Object>> message) {
        return violationMessage(annotationType, Object.class, message);
    }

    public <A extends Annotation, T> JakartaSettings<SENDER> violationMessage(Class<A> annotationType, Class<T> type, InvokedMessage<SENDER, ?, JakartaViolation<A, T>> message) {
        this.violationMessages.put(annotationType, type, message);
        return this;
    }

    public JakartaSettings<SENDER> violationMessage(InvokedMessage<SENDER, ?, JakartaViolation<?, ?>> message) {
        this.defaultViolationMessage = message;
        return this;
    }

    @SuppressWarnings("unchecked")
    <A extends Annotation, T> Object toMessage(Invocation<SENDER> invocation, JakartaViolation<A, T> violation) {
        InvokedMessage<SENDER, ?, JakartaViolation<A, T>> message = (InvokedMessage<SENDER, ?, JakartaViolation<A, T>>) violationMessages.get(violation.getAnnotationType(), violation.getInvalidValue().getClass());

        if (message == null) {
            message = (InvokedMessage<SENDER, ?, JakartaViolation<A, T>>) violationMessages.get(violation.getAnnotationType(), Object.class);
        }

        if (message == null) {
            return defaultViolationMessage.get(invocation, violation);
        }

        return message.get(invocation, violation);
    }

}
