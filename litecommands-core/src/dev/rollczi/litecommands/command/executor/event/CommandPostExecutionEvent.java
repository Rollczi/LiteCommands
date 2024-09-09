package dev.rollczi.litecommands.command.executor.event;

import dev.rollczi.litecommands.command.executor.CommandExecuteResult;
import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.invocation.Invocation;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public class CommandPostExecutionEvent implements CommandExecutorEvent {

    private final Invocation<?> invocation;
    private final CommandExecutor<?> executor;
    private final CommandExecuteResult result;

    public CommandPostExecutionEvent(Invocation<?> invocation, CommandExecutor<?> executor, CommandExecuteResult result) {
        this.invocation = invocation;
        this.executor = executor;
        this.result = result;
    }

    @Override
    public <T> Invocation<T> getInvocation() {
        return (Invocation<T>) invocation;
    }

    @Override
    public <SENDER> CommandExecutor<SENDER> getExecutor() {
        return (CommandExecutor<SENDER>) executor;
    }

    public CommandExecuteResult getResult() {
        return result;
    }

}
