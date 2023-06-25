package dev.rollczi.litecommands.command.executor;

import dev.rollczi.litecommands.command.CommandRoute;

@FunctionalInterface
public interface CommandExecutorFactory<SENDER> {

    CommandExecutor<SENDER, ?> create(CommandRoute<SENDER> parent);

}
