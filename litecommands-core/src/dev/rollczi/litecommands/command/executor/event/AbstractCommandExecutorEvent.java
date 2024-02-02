package dev.rollczi.litecommands.command.executor.event;

import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.invocation.Invocation;

public class AbstractCommandExecutorEvent {

    private final Invocation<?> invocation;
    private final CommandExecutor<?> executor;

    public AbstractCommandExecutorEvent(Invocation<?> invocation, CommandExecutor<?> executor) {
        this.invocation = invocation;
        this.executor = executor;
    }

    public Invocation<?> getInvocation() {
        return invocation;
    }

    public CommandExecutor<?> getExecutor() {
        return executor;
    }

}
