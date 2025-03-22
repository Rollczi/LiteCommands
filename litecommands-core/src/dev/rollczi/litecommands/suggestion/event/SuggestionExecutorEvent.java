package dev.rollczi.litecommands.suggestion.event;

import dev.rollczi.litecommands.command.CommandNode;
import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.invocation.Invocation;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public class SuggestionExecutorEvent implements SuggestionNodeEvent {

    private final Invocation<?> invocation;
    private final CommandExecutor<?> executor;
    private boolean isCancelled = false;

    public SuggestionExecutorEvent(Invocation<?> invocation, CommandExecutor<?> executor) {
        this.invocation = invocation;
        this.executor = executor;
    }

    public CommandExecutor<?> getCommandExecutor() {
        return executor;
    }

    public Invocation<?> getInvocation() {
        return invocation;
    }

    @Override
    public CommandNode<?> getNode() {
        return executor;
    }

    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

}
