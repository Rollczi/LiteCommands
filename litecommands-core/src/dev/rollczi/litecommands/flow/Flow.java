package dev.rollczi.litecommands.flow;

import dev.rollczi.litecommands.argument.FailedReason;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Supplier;

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

    boolean hasReason();

    <T> T flow(
        Supplier<T> continueFlow,
        Function<Object, T> stopCurrentFlow,
        Function<Object, T> terminateFlow
    );

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
        return new FlowImpl(Status.CONTINUE, null);
    }

    static Flow stopCurrentFlow(Object reason) {
        return new FlowImpl(Status.STOP_CURRENT, reason);
    }

    static Flow terminateFlow(Object reason) {
        return new FlowImpl(Status.TERMINATE, reason);
    }

    static <E> Flow merge(Iterable<E> iterable, FlowMapper<E> mapper) {
        Flow lastStopped = null;

        for (E element : iterable) {
            Flow flow = mapper.map(element);

            switch (flow.status()) {
                case CONTINUE: continue;
                case TERMINATE: return flow;
                case STOP_CURRENT: lastStopped = flow;
            }
        }

        if (lastStopped != null) {
            return lastStopped;
        }

        return Flow.continueFlow();
    }


}
