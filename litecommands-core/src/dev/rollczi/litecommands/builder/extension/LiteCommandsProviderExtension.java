package dev.rollczi.litecommands.builder.extension;

import dev.rollczi.litecommands.builder.LiteCommandsBuilder;
import dev.rollczi.litecommands.builder.LiteCommandsInternalBuilderApi;
import dev.rollczi.litecommands.builder.LiteCommandsProvider;

public interface LiteCommandsProviderExtension<SENDER> extends LiteExtension<SENDER> {

    void extendCommandsProvider(LiteCommandsBuilder<SENDER, ?, ?> builder, LiteCommandsInternalBuilderApi<SENDER, ?> pattern, LiteCommandsProvider<SENDER> provider);

}
