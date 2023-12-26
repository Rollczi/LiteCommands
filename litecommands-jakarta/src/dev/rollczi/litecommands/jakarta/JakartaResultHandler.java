package dev.rollczi.litecommands.jakarta;

import static java.util.stream.Collectors.joining;

import dev.rollczi.litecommands.handler.result.ResultHandler;
import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.message.MessageKey;
import dev.rollczi.litecommands.message.MessageRegistry;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.MessageInterpolator;
import java.util.Locale;
import java.util.function.Function;

class JakartaResultHandler<SENDER> implements ResultHandler<SENDER, JakartaResult> {

    private static final String NEW_LINE_CHARACTER = "\n";
    private static final String CONSTRAINT_VIOLATION_PARAMETER_NAME_SEPARATOR = "\\.";
    private static final String PARAMETER_BRACKET_OPEN = "<";
    private static final String PARAMETER_BRACKET_CLOSE = ">";
    private final Function<Invocation<SENDER>, Locale> localeProvider;
    private final MessageRegistry<SENDER> messageRegistry;
    private final MessageInterpolator messageInterpolator;
    private final String constraintViolationDelimiter;
    private final MessageKey<String> constrainsViolationMessage;

    JakartaResultHandler(
        Function<Invocation<SENDER>, Locale> localeProvider,
        MessageRegistry<SENDER> messageRegistry,
        MessageInterpolator messageInterpolator,
        String constraintViolationDelimiter,
        MessageKey<String> constrainsViolationMessage
    ) {
        this.localeProvider = localeProvider;
        this.messageRegistry = messageRegistry;
        this.messageInterpolator = messageInterpolator;
        this.constraintViolationDelimiter = constraintViolationDelimiter;
        this.constrainsViolationMessage = constrainsViolationMessage;
    }

    @Override
    public void handle(Invocation<SENDER> invocation, JakartaResult result, ResultHandlerChain<SENDER> chain) {
        messageRegistry.getInvoked(constrainsViolationMessage, invocation,
                result.getViolations().stream()
                    .map(violation -> getViolationMessage(violation, localeProvider.apply(invocation)))
                    .collect(joining(NEW_LINE_CHARACTER)))
            .ifPresent(object -> chain.resolve(invocation, object));
    }

    private String getViolationMessage(ConstraintViolation<Object> violation, Locale locale) {
        return getParameterName(violation) + constraintViolationDelimiter + getInterpolatedMessage(violation, locale);
    }

    private String getInterpolatedMessage(ConstraintViolation<Object> violation, Locale locale) {
        return messageInterpolator.interpolate(
            violation.getMessageTemplate(), new JakartaConstraintViolationContext(violation), locale);
    }

    private String getParameterName(ConstraintViolation<Object> violation) {
        return String.format("%s%s%s",
            PARAMETER_BRACKET_OPEN, getParameterNameFromViolation(violation), PARAMETER_BRACKET_CLOSE);
    }

    private String getParameterNameFromViolation(ConstraintViolation<Object> violation) {
        return violation.getPropertyPath()
            .toString()
            .split(CONSTRAINT_VIOLATION_PARAMETER_NAME_SEPARATOR)[1];
    }

}
