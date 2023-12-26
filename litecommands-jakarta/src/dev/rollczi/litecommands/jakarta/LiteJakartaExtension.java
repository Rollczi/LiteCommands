package dev.rollczi.litecommands.jakarta;

import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.LiteCommandsInternal;
import dev.rollczi.litecommands.configurator.LiteConfigurator;
import dev.rollczi.litecommands.extension.LiteExtension;

import java.util.function.UnaryOperator;

public class LiteJakartaExtension<SENDER> implements LiteExtension<SENDER, JakartaSettings> {

    private final JakartaSettings settings = new JakartaSettings();

    @Override
    public void configure(LiteConfigurator<JakartaSettings> configurer) {
        configurer.configure(settings);
    }

    @Override
    public void extend(LiteCommandsBuilder<SENDER, ?, ?> builder, LiteCommandsInternal<SENDER, ?> internal) {
        builder
            .result(JakartaResult.class,
                new JakartaResultHandler<>(
                    settings.locale,
                    internal.getMessageRegistry(),
                    settings.validatorFactory.getMessageInterpolator(),
                    settings.constraintViolationDelimiter))
            .annotations(configuration -> configuration
                .methodValidator(new JakartaMethodValidator<>(settings.validatorFactory.getValidator())));
    }

    public LiteJakartaExtension<SENDER> settings(UnaryOperator<JakartaSettings> settings) {
        settings.apply(this.settings);
        return this;
    }

}
