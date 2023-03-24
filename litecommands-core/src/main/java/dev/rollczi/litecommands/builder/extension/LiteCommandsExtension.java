package dev.rollczi.litecommands.builder.extension;

import dev.rollczi.litecommands.builder.LiteCommandsBuilder;
import dev.rollczi.litecommands.builder.LiteCommandsInternalBuilderApi;
import dev.rollczi.litecommands.platform.LiteSettings;

public interface LiteCommandsExtension<SENDER, C extends LiteSettings> {

    void extend(LiteCommandsBuilder<SENDER, C, ?> builder, LiteCommandsInternalBuilderApi<SENDER, ?> pattern);

}
