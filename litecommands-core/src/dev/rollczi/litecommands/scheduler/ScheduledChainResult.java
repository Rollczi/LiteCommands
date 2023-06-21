package dev.rollczi.litecommands.scheduler;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ScheduledChainResult<E> {

    private final List<E> results;
    private final @Nullable ScheduledChainException exception;

    private ScheduledChainResult(List<E> results, @Nullable ScheduledChainException exception) {
        this.results = results;
        this.exception = exception;
    }

    public List<E> getResults() {
        return results;
    }

    public @Nullable ScheduledChainException getException() {
        return exception;
    }

    public boolean isSuccess() {
        return exception == null;
    }

    public boolean isFailure() {
        return exception != null;
    }

    public static <E> ScheduledChainResult<E> success(List<E> results) {
        return new ScheduledChainResult<>(results, null);
    }

    public static <E> ScheduledChainResult<E> failure(ScheduledChainException exception) {
        return new ScheduledChainResult<>(null, exception);
    }

}
