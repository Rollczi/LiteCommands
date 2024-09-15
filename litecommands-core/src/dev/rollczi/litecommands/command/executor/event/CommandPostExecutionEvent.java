package dev.rollczi.litecommands.command.executor.event;

import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.command.executor.CommandExecuteResult;
import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.shared.Preconditions;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Experimental
public class CommandPostExecutionEvent<SENDER> implements CommandExecutorEvent<SENDER> {

    private final Invocation<SENDER> invocation;
    private final CommandRoute<SENDER> route;
    private final @Nullable CommandExecutor<SENDER> executor;
    private CommandExecuteResult result;

    public CommandPostExecutionEvent(Invocation<SENDER> invocation, CommandRoute<SENDER> route, @Nullable CommandExecutor<SENDER> executor, CommandExecuteResult result) {
        this.invocation = invocation;
        this.route = route;
        this.executor = executor;
        this.result = result;
    }

    @Override
    public Invocation<SENDER> getInvocation() {
        return invocation;
    }

    public CommandRoute<SENDER> getCommandRoute() {
        return route;
    }

    @Nullable
    @Override
    public CommandExecutor<SENDER> getExecutor() {
        return executor;
    }

    public CommandExecuteResult getResult() {
        return result;
    }

    public void setResult(CommandExecuteResult result) {
        Preconditions.notNull(result, "result cannot be null");
        this.result = result;
    }

}
