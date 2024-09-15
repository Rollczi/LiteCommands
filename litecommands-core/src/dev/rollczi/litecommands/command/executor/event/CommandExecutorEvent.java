package dev.rollczi.litecommands.command.executor.event;

import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.event.Event;
import dev.rollczi.litecommands.invocation.Invocation;
import org.jetbrains.annotations.ApiStatus;

/**
 * Represents an event fired when a command executor is executed.
 * Flow:
 * - {@link CandidateExecutorFoundEvent}
 * - {@link CandidateExecutorMatchEvent}
 * - {@link CommandPreExecutionEvent}
 * - {@link CommandPostExecutionEvent}
 */
@ApiStatus.Experimental
public interface CommandExecutorEvent<SENDER> extends Event {

    Invocation<SENDER> getInvocation();

    CommandExecutor<SENDER> getExecutor();

}
