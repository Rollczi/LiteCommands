package dev.rollczi.litecommands.scheduler;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ScheduledChainResult<E> {

    private final List<E> results;
    private final @Nullable Object failureReason;

    private ScheduledChainResult(List<E> results, @Nullable Object failureReason) {
        this.results = results;
        this.failureReason = failureReason;
    }

    public List<E> getSuccess() {
        return results;
    }

    public @Nullable Object getFailure() {
        return failureReason;
    }

    public boolean isSuccess() {
        return failureReason == null;
    }

    public boolean isFailure() {
        return failureReason != null;
    }

    public static <E> ScheduledChainResult<E> success(List<E> results) {
        return new ScheduledChainResult<>(results, null);
    }

    public static <E> ScheduledChainResult<E> failure(Object failureReason) {
        return new ScheduledChainResult<>(null, failureReason);
    }

}
