package dev.rollczi.litecommands.flow;

import dev.rollczi.litecommands.shared.FailedReason;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Supplier;

class FlowImpl implements Flow {

    private final Status status;
    private final FailedReason failedReason;

    FlowImpl(Status status, @Nullable Object reason) {
        this.status = status;
        this.failedReason = FailedReason.of(reason);
    }

    @Override
    public Status status() {
        return status;
    }

    @Override
    public boolean isContinue() {
        return status == Status.CONTINUE;
    }

    @Override
    public boolean isStopCurrent() {
        return status == Status.STOP_CURRENT;
    }

    @Override
    public boolean isTerminate() {
        return status == Status.TERMINATE;
    }

    @Override
    public @Nullable Object getReason() {
        return failedReason.getReason();
    }

    @Override
    public FailedReason failedReason() {
        return failedReason;
    }

    @Override
    public boolean hasReason() {
        return !failedReason.isEmpty();
    }

    @Override
    public <T> T flow(Supplier<T> continueFlow, Function<Object, T> stopCurrentFlow, Function<Object, T> terminateFlow) {
        return null;
    }

}
