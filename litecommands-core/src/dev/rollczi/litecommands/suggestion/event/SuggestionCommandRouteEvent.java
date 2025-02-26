package dev.rollczi.litecommands.suggestion.event;

import dev.rollczi.litecommands.command.CommandNode;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.invocation.Invocation;

public class SuggestionCommandRouteEvent implements SuggestionNodeEvent {

    private final Invocation<?> invocation;
    private final CommandRoute<?> commandRoute;
    private boolean isCancelled = false;

    public SuggestionCommandRouteEvent(Invocation<?> invocation, CommandRoute<?> commandRoute) {
        this.invocation = invocation;
        this.commandRoute = commandRoute;
    }

    public CommandRoute<?> getCommandRoute() {
        return commandRoute;
    }

    public Invocation<?> getInvocation() {
        return invocation;
    }

    @Override
    public CommandNode<?> getNode() {
        return commandRoute;
    }

    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

}
