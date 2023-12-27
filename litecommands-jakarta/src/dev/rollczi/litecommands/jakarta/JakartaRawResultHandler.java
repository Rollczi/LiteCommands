package dev.rollczi.litecommands.jakarta;

import dev.rollczi.litecommands.handler.result.ResultHandler;
import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.schematic.SchematicFormat;
import dev.rollczi.litecommands.schematic.SchematicFormatProvider;
import dev.rollczi.litecommands.schematic.SchematicGenerator;
import jakarta.validation.ConstraintViolation;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

class JakartaRawResultHandler<SENDER> implements ResultHandler<SENDER, JakartaRawResult> {

    private final JakartaSettings<SENDER> settings;
    private final SchematicFormat schematicFormat;

    JakartaRawResultHandler(JakartaSettings<SENDER> settings, SchematicGenerator<SENDER> schematicGenerator) {
        this.settings = settings;
        this.schematicFormat = schematicGenerator instanceof SchematicFormatProvider
            ? ((SchematicFormatProvider) schematicGenerator).getFormat()
            : SchematicFormat.angleBrackets();
    }

    @Override
    public void handle(Invocation<SENDER> invocation, JakartaRawResult result, ResultHandlerChain<SENDER> chain) {
        List<String> list = result.getViolations().stream()
            .map(entry -> getViolation(entry, settings.localeProvider.apply(invocation)))
            .map(violation -> settings.toMessage(invocation, violation))
            .collect(Collectors.toList());

        Object message = settings.constraintViolationsMessage.get(invocation, new JakartaParsedResult(list));

        chain.resolve(invocation, message);
    }

    private JakartaViolation<?> getViolation(JakartaRawResult.Entry entry, Locale locale) {
        ConstraintViolation<Object> constraintViolation = entry.getViolation();

        return new JakartaViolation<>(
            constraintViolation.getConstraintDescriptor().getAnnotation().annotationType(),
            constraintViolation,
            entry.getRequirement(),
            getInterpolatedMessage(constraintViolation, locale),
            entry.getRequirement().getName(),
            schematicFormat, locale
        );
    }

    private String getInterpolatedMessage(ConstraintViolation<Object> violation, Locale locale) {
        return settings.validatorFactory.getMessageInterpolator().interpolate(
            violation.getMessageTemplate(), new JakartaConstraintViolationContext(violation), locale);
    }

}
