package dev.rollczi.litecommands.jakarta;

import static java.util.stream.Collectors.joining;

import dev.rollczi.litecommands.handler.result.ResultHandler;
import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.message.MessageRegistry;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.MessageInterpolator;
import java.util.Locale;

class JakartaResultHandler<SENDER> implements ResultHandler<SENDER, JakartaResult> {

    private static final String NEW_LINE_CHARACTER = "\n";
    private static final String CONSTRAINT_VIOLATION_PARAMETER_NAME_SEPARATOR = "\\.";
    private static final String PARAMETER_BRACKET_OPEN = "<";
    private static final String PARAMETER_BRACKET_CLOSE = ">";
    private final Locale locale;
    private final MessageRegistry<SENDER> messageRegistry;
    private final MessageInterpolator messageInterpolator;
    private final String constraintViolationDelimiter;

    JakartaResultHandler(
        Locale locale,
        MessageRegistry<SENDER> messageRegistry,
        MessageInterpolator messageInterpolator,
        String constraintViolationDelimiter
    ) {
        this.locale = locale;
        this.messageRegistry = messageRegistry;
        this.messageInterpolator = messageInterpolator;
        this.constraintViolationDelimiter = constraintViolationDelimiter;
    }

    @Override
    public void handle(Invocation<SENDER> invocation, JakartaResult result, ResultHandlerChain<SENDER> chain) {
        messageRegistry.getInvoked(LiteJakartaMessages.CONSTRAINT_VIOLATIONS, invocation,
                result.getViolations().stream()
                    .map(this::getViolationMessage)
                    .collect(joining(NEW_LINE_CHARACTER)))
            .ifPresent(object -> chain.resolve(invocation, object));
    }

    private String getViolationMessage(final ConstraintViolation<Object> violation) {
        return getParameterName(violation) + constraintViolationDelimiter + getInterpolatedMessage(violation);
    }

    private String getInterpolatedMessage(final ConstraintViolation<Object> violation) {
        return messageInterpolator.interpolate(
            violation.getMessageTemplate(), new JakartaConstraintViolationContext(violation), locale);
    }

    private String getParameterName(final ConstraintViolation<Object> violation) {
        return String.format("%s%s%s",
            PARAMETER_BRACKET_OPEN, getParameterNameFromViolation(violation), PARAMETER_BRACKET_CLOSE);
    }

    private String getParameterNameFromViolation(final ConstraintViolation<Object> violation) {
        return violation.getPropertyPath()
            .toString()
            .split(CONSTRAINT_VIOLATION_PARAMETER_NAME_SEPARATOR)[1];
    }

}
