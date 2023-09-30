package dev.rollczi.litecommands.builder.extension;

import dev.rollczi.litecommands.builder.LiteCommandsBuilder;
import dev.rollczi.litecommands.builder.LiteCommandsInternalBuilderApi;

public interface LiteExtension<SENDER> {

    void extend(LiteCommandsBuilder<SENDER, ?, ?> builder, LiteCommandsInternalBuilderApi<SENDER, ?> pattern);

}
