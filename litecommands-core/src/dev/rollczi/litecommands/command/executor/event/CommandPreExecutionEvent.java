package dev.rollczi.litecommands.command.executor.event;

import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.invocation.Invocation;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public class CommandPreExecutionEvent<SENDER> extends AbstractCommandExecutorEvent<SENDER> implements CommandExecutorEvent<SENDER> {

    public CommandPreExecutionEvent(Invocation<SENDER> invocation, CommandExecutor<SENDER> executor) {
        super(invocation, executor);
    }

}
