package dev.rollczi.litecommands.context;

import dev.rollczi.litecommands.requirement.RequirementResult;
import java.util.function.Function;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class ContextResult<T> implements RequirementResult<T> {

    private final @Nullable Supplier<T> result;
    private final Object error;

    private ContextResult(@Nullable Supplier<T> result, Object error) {
        this.result = result;
        this.error = error;
    }

    @Override
    public @NotNull T getSuccess() {
        if (result == null) {
            throw new IllegalStateException("Cannot get success result from failed result");
        }

        return result.get();
    }

    @Override
    public @NotNull Object getFailedReason() {
        return error;
    }

    @Override
    public boolean isFailed() {
        return error != null;
    }

    @Override
    public boolean isSuccessful() {
        return result != null;
    }

    @Override
    public boolean isSuccessfulNull() {
        return false;
    }

    @ApiStatus.Experimental
    public <R> ContextResult<R> map(Function<T, R> mapper) {
        if (this.isFailed()) {
            return ContextResult.error(error);
        }

        return ContextResult.ok(() -> mapper.apply(getSuccess()));
    }

    @ApiStatus.Experimental
    public <R> ContextResult<R> flatMap(Function<T, ContextResult<R>> mapper) {
        if (this.isFailed()) {
            return ContextResult.error(error);
        }

        return mapper.apply(getSuccess());
    }

    public static <T> ContextResult<T> ok(Supplier<T> supplier) {
        return new ContextResult<>(supplier, null);
    }

    public static <T> ContextResult<T> error(Object error) {
        return new ContextResult<>(null, error);
    }

}
