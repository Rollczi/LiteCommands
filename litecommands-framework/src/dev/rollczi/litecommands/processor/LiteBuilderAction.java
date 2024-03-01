package dev.rollczi.litecommands.processor;

import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.LiteCommandsInternal;
import dev.rollczi.litecommands.platform.PlatformSettings;

@FunctionalInterface
public interface LiteBuilderAction<SENDER, SETTINGS extends PlatformSettings> {

    void process(LiteCommandsBuilder<SENDER, SETTINGS, ?> builder, LiteCommandsInternal<SENDER, SETTINGS> internal);

}