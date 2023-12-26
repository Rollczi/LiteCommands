package dev.rollczi.litecommands.jakarta;

import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.LiteCommandsInternal;
import dev.rollczi.litecommands.configurator.LiteConfigurator;
import dev.rollczi.litecommands.extension.LiteExtension;

import java.util.function.UnaryOperator;

public class LiteJakartaExtension<SENDER> implements LiteExtension<SENDER, JakartaSettings<SENDER>> {

    private final JakartaSettings<SENDER> settings = new JakartaSettings<>();

    @Override
    public void configure(LiteConfigurator<JakartaSettings<SENDER>> configurer) {
        configurer.configure(settings);
    }

    @Override
    public void extend(LiteCommandsBuilder<SENDER, ?, ?> builder, LiteCommandsInternal<SENDER, ?> internal) {
        builder
            .result(JakartaResult.class,
                new JakartaResultHandler<>(
                    settings.localeProvider,
                    internal.getMessageRegistry(),
                    settings.validatorFactory.getMessageInterpolator(),
                    settings.constraintViolationDelimiter,
                    settings.constrainsViolationMessage))
            .annotations(configuration -> configuration
                .methodValidator(new JakartaMethodValidator<>(settings.validatorFactory.getValidator())));
    }

    public LiteJakartaExtension<SENDER> settings(UnaryOperator<JakartaSettings<SENDER>> settings) {
        settings.apply(this.settings);
        return this;
    }

}
