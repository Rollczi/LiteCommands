package dev.rollczi.litecommands.suggestion.event;

import dev.rollczi.litecommands.command.CommandNode;
import dev.rollczi.litecommands.event.Event;
import dev.rollczi.litecommands.invocation.Invocation;

public interface SuggestionNodeEvent extends Event {

    Invocation<?> getInvocation();

    CommandNode<?> getNode();

    void setCancelled(boolean cancelled);

    boolean isCancelled();

}
