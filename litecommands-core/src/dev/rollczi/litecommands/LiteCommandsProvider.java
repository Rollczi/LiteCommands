package dev.rollczi.litecommands;

import dev.rollczi.litecommands.command.builder.CommandBuilder;
import dev.rollczi.litecommands.command.builder.CommandBuilderProvider;

import java.util.List;

public interface LiteCommandsProvider<SENDER> {

    List<CommandBuilder<SENDER>> provide(LiteCommandsInternal<SENDER, ?> builder);

    default CommandBuilderProvider<SENDER> toInternalProvider(LiteCommandsInternal<SENDER, ?> builder) {
        return () -> LiteCommandsProvider.this.provide(builder);
    }

}
