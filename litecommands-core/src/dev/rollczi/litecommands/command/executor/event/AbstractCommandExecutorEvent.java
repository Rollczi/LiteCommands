package dev.rollczi.litecommands.command.executor.event;

import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.command.executor.flow.ExecuteFlow;
import dev.rollczi.litecommands.command.executor.flow.ExecuteFlowEvent;
import dev.rollczi.litecommands.event.Event;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.shared.FailedReason;
import dev.rollczi.litecommands.shared.Preconditions;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Experimental
abstract class AbstractCommandExecutorEvent<SENDER> implements Event, CommandExecutorEvent<SENDER>, ExecuteFlowEvent<FailedReason> {

    private final Invocation<SENDER> invocation;
    private final CommandExecutor<SENDER> executor;
    private ExecuteFlow flow = ExecuteFlow.CONTINUE;
    private FailedReason cancelReason;

    protected AbstractCommandExecutorEvent(Invocation<SENDER> invocation, CommandExecutor<SENDER> executor) {
        this.invocation = invocation;
        this.executor = executor;
    }

    @Override
    public Invocation<SENDER> getInvocation() {
        return invocation;
    }

    @Override
    public CommandExecutor<SENDER> getExecutor() {
        return executor;
    }

    @Override
    public ExecuteFlow getFlow() {
        return flow;
    }

    @Override
    public @Nullable FailedReason getFlowResult() {
        if (this.flow == ExecuteFlow.CONTINUE) {
            throw new IllegalStateException("Cannot get cancel reason when flow is not TERMINATE or SKIP");
        }

        return cancelReason;
    }

    @Override
    public void continueFlow() {
        this.flow = ExecuteFlow.CONTINUE;
        this.cancelReason = null;
    }

    @Override
    public void stopFlow(FailedReason reason) {
        Preconditions.notNull(reason, "reason");
        this.flow = ExecuteFlow.STOP;
        this.cancelReason = reason;
    }

    @Override
    public void skipFlow(FailedReason reason) {
        Preconditions.notNull(reason, "reason");
        this.flow = ExecuteFlow.SKIP;
        this.cancelReason = reason;
    }

}
