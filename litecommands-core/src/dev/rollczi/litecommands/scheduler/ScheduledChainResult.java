package dev.rollczi.litecommands.scheduler;

import dev.rollczi.litecommands.shared.LiteResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ScheduledChainResult<E> implements LiteResult<List<E>, ScheduledChainException> {

    private final List<E> results;
    private final @Nullable ScheduledChainException exception;

    private ScheduledChainResult(List<E> results, @Nullable ScheduledChainException exception) {
        this.results = results;
        this.exception = exception;
    }

    @Override
    public List<E> getSuccess() {
        return results;
    }

    @Override
    public @Nullable ScheduledChainException getFailure() {
        return exception;
    }

    @Override
    public boolean isSuccess() {
        return exception == null;
    }

    @Override
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
