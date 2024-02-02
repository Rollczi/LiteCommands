package dev.rollczi.litecommands.command.executor.event;

import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.invocation.Invocation;

public class CommandExecutionCompletionEvent extends AbstractCommandExecutorEvent {

    public CommandExecutionCompletionEvent(Invocation<?> invocation, CommandExecutor<?> executor) {
        super(invocation, executor);
    }

}
