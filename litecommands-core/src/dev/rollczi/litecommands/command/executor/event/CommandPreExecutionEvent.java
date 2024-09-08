package dev.rollczi.litecommands.command.executor.event;

import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.invocation.Invocation;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public class CommandPreExecutionEvent extends AbstractCommandExecutorEvent implements CommandExecutorEvent {

    public CommandPreExecutionEvent(Invocation<?> invocation, CommandExecutor<?> executor) {
        super(invocation, executor);
    }

}
