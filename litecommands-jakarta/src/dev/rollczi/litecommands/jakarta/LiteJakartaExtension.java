package dev.rollczi.litecommands.jakarta;

import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.LiteCommandsInternal;
import dev.rollczi.litecommands.configurator.LiteConfigurator;
import dev.rollczi.litecommands.extension.LiteExtension;

public class LiteJakartaExtension<SENDER> implements LiteExtension<SENDER, JakartaSettings<SENDER>> {

    private final JakartaSettings<SENDER> settings = new JakartaSettings<>();

    @Override
    public void configure(LiteConfigurator<JakartaSettings<SENDER>> configurer) {
        configurer.configure(settings);
    }

    @Override
    public void extend(LiteCommandsBuilder<SENDER, ?, ?> builder, LiteCommandsInternal<SENDER, ?> internal) {
        builder
            .result(JakartaRawResult.class, new JakartaRawResultHandler<>(settings, internal.getSchematicGenerator()))
            .annotations(configuration -> configuration
                .methodValidator(new JakartaMethodValidator<>(settings.validatorFactory.getValidator())));
    }

}
