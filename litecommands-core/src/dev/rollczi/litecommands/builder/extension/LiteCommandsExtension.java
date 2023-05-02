package dev.rollczi.litecommands.builder.extension;

import dev.rollczi.litecommands.builder.LiteCommandsBuilder;
import dev.rollczi.litecommands.builder.LiteCommandsInternalBuilderApi;
import dev.rollczi.litecommands.platform.LiteSettings;

public interface LiteCommandsExtension<SENDER> {

    void extend(LiteCommandsBuilder<SENDER, ?, ?> builder, LiteCommandsInternalBuilderApi<SENDER, ?> pattern);

}
