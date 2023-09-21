package dev.rollczi.litecommands.builder;

import dev.rollczi.litecommands.command.builder.CommandBuilder;
import dev.rollczi.litecommands.command.builder.CommandBuilderProvider;

import java.util.List;

@Deprecated
public interface LiteCommandsProvider<SENDER> {

    List<CommandBuilder<SENDER>> provide(LiteCommandsInternalBuilderApi<SENDER, ?> builder);

    default CommandBuilderProvider<SENDER> toInternalProvider(LiteCommandsInternalBuilderApi<SENDER, ?> builder) {
        return () -> LiteCommandsProvider.this.provide(builder);
    }

}
