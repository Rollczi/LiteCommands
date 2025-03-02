package dev.rollczi.litecommands.flow;

import dev.rollczi.litecommands.shared.FailedReason;
import org.jetbrains.annotations.Nullable;

@Deprecated //TODO is used without context of current flow, replace with e.g for Validation domain with ValidatorResult
public interface Flow {

    Status status();

    boolean isContinue();

    boolean isStopCurrent();

    boolean isTerminate();

    /**
     * Reason why flow was stopped or terminated
     * if flow is CONTINUE then reason is null
     * if flow is STOP_CURRENT or TERMINATED then reason is not null
     *
     * @return reason
     */
    @Nullable Object getReason();

    FailedReason failedReason();

    /**
     * Flow status
     * CONTINUE - continue current flow
     * STOPPED - stop current flow and continue next flow
     * TERMINATED - stop current flow and stop next flow
     */
    enum Status {
        CONTINUE,
        STOP_CURRENT,
        TERMINATE,
    }

    static Flow continueFlow() {
        return new FlowImpl(Status.CONTINUE, "NONE");
    }

    static Flow stopCurrentFlow(Object reason) {
        return new FlowImpl(Status.STOP_CURRENT, reason);
    }

    static Flow terminateFlow(Object reason) {
        return new FlowImpl(Status.TERMINATE, reason);
    }

}
