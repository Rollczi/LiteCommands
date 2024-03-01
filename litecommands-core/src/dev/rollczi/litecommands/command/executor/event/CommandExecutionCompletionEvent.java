package dev.rollczi.litecommands.command.executor.event;

import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.invocation.Invocation;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public class CommandExecutionCompletionEvent implements CommandExecutorEvent {

    private final Invocation<?> invocation;
    private final CommandExecutor<?> executor;

    public CommandExecutionCompletionEvent(Invocation<?> invocation, CommandExecutor<?> executor) {
        this.invocation = invocation;
        this.executor = executor;
    }

    @Override
    public Invocation<?> getInvocation() {
        return invocation;
    }

    @Override
    public CommandExecutor<?> getExecutor() {
        return executor;
    }

}
