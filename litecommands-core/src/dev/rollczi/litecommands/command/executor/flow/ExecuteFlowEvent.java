package dev.rollczi.litecommands.command.executor.flow;

import dev.rollczi.litecommands.event.Event;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public interface ExecuteFlowEvent<R> extends Event {

    ExecuteFlow getFlow();

    R getFlowResult();

    void continueFlow();

    void terminateFlow(R result);

    void skipFlow(R result);

}
