package dev.rollczi.litecommands.command.executor.event;

import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.event.Event;
import dev.rollczi.litecommands.invocation.Invocation;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public interface CommandExecutorEvent extends Event {

    Invocation<?> getInvocation();

    CommandExecutor<?> getExecutor();

}
