package dev.rollczi.litecommands.builder.processor;

import dev.rollczi.litecommands.builder.LiteCommandsBuilder;
import dev.rollczi.litecommands.builder.LiteCommandsInternalBuilderApi;
import dev.rollczi.litecommands.platform.LiteSettings;

@FunctionalInterface
public interface LiteBuilderPostProcessor<SENDER, SETTINGS extends LiteSettings> {

    void process(LiteCommandsBuilder<SENDER, SETTINGS, ?> builder, LiteCommandsInternalBuilderApi<SENDER, SETTINGS> pattern);

    static <SENDER, SETTINGS extends LiteSettings> LiteBuilderPostProcessor<SENDER, SETTINGS> empty() {
        return (builder, pattern) -> {};
    }

}