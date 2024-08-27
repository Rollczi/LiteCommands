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
        List<Object> list = result.getViolations().stream()
            .map(entry -> getViolation(entry, settings.localeProvider.apply(invocation)))
            .map(violation -> settings.toMessage(invocation, violation))
            .collect(Collectors.toList());

        chain.resolve(invocation, list);
    }

    private JakartaViolation<?, ?> getViolation(JakartaRawResult.Entry entry, Locale locale) {
        ConstraintViolation<Object> violation = entry.getViolation();

        return new JakartaViolation<>(
            violation.getConstraintDescriptor().getAnnotation().annotationType(),
            violation.getInvalidValue(),
            violation,
            entry.getRequirement(),
            getInterpolatedMessage(violation, locale),
            entry.getRequirement().getName(),
            schematicFormat, locale
        );
    }

    private String getInterpolatedMessage(ConstraintViolation<Object> violation, Locale locale) {
        return settings.validatorFactory.getMessageInterpolator().interpolate(
            violation.getMessageTemplate(), new JakartaConstraintViolationContext(violation), locale);
    }

}
