package dev.rollczi.litecommands.command;

import dev.rollczi.litecommands.command.executor.CommandExecutor;

public interface CommandExecutorProvider<SENDER> {

    CommandExecutor<SENDER> provide(CommandRoute<SENDER> parent);

}
