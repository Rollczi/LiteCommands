package dev.rollczi.litecommands.command.executor.event;

import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.scheduler.SchedulerType;
import dev.rollczi.litecommands.shared.FailedReason;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public class CommandPreExecutionEvent<SENDER> implements CommandExecutorEvent<SENDER> {

    private final Invocation<SENDER> invocation;
    private final CommandExecutor<SENDER> executor;
    private FailedReason cancelReason;
    private SchedulerType schedulerType;

    public CommandPreExecutionEvent(Invocation<SENDER> invocation, CommandExecutor<SENDER> executor) {
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

    public SchedulerType getSchedulerType() {
        if (schedulerType == null) {
            return executor.metaCollector().findFirst(Meta.POLL_TYPE);
        }
        return schedulerType;
    }

    public void setSchedulerType(SchedulerType schedulerType) {
        this.schedulerType = schedulerType;
    }

    public void cancel(FailedReason reason) {
        this.cancelReason = reason;
    }

    public void allow() {
        this.cancelReason = null;
    }

    public boolean isCancelled() {
        return this.cancelReason != null;
    }

    public FailedReason getCancelReason() {
        return this.cancelReason;
    }

}
