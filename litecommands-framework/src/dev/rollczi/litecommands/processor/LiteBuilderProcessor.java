package dev.rollczi.litecommands.processor;

import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.builder.LiteCommandsInternalBuilderApi;
import dev.rollczi.litecommands.platform.PlatformSettings;

@FunctionalInterface
public interface LiteBuilderProcessor<SENDER, SETTINGS extends PlatformSettings> {

    void process(LiteCommandsBuilder<SENDER, SETTINGS, ?> builder, LiteCommandsInternalBuilderApi<SENDER, SETTINGS> pattern);

}