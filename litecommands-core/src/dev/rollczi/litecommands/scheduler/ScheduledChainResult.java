package dev.rollczi.litecommands.scheduler;

import dev.rollczi.litecommands.shared.LiteResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ScheduledChainResult<E> implements LiteResult<List<E>, Object> {

    private final List<E> results;
    private final @Nullable Object failureReason;

    private ScheduledChainResult(List<E> results, @Nullable Object failureReason) {
        this.results = results;
        this.failureReason = failureReason;
    }

    @Override
    public List<E> getSuccess() {
        return results;
    }

    @Override
    public @Nullable Object getFailure() {
        return failureReason;
    }

    @Override
    public boolean isSuccess() {
        return failureReason == null;
    }

    @Override
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
