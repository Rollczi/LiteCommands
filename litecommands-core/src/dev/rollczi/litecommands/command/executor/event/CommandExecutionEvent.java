package dev.rollczi.litecommands.command.executor.event;

import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.invocation.Invocation;

public class CommandExecutionEvent extends AbstractCommandExecutorEvent {

    public CommandExecutionEvent(Invocation<?> invocation, CommandExecutor<?> executor) {
        super(invocation, executor);
    }

}
