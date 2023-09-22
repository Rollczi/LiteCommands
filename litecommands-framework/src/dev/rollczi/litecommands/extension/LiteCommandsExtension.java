package dev.rollczi.litecommands.extension;

import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.builder.LiteCommandsInternalBuilderApi;

public interface LiteCommandsExtension<SENDER> {

    void extend(LiteCommandsBuilder<SENDER, ?, ?> builder, LiteCommandsInternalBuilderApi<SENDER, ?> pattern);

}
