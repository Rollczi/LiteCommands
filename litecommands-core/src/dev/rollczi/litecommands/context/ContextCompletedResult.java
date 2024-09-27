package dev.rollczi.litecommands.context;

import dev.rollczi.litecommands.requirement.RequirementCondition;
import dev.rollczi.litecommands.requirement.RequirementResult;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ContextCompletedResult<T> implements ContextResult<T>, RequirementResult<T> {

    private final @Nullable Supplier<T> result;
    private final List<RequirementCondition> conditions;
    private final Object error;

    ContextCompletedResult(@Nullable Supplier<T> result, Object error, List<RequirementCondition> conditions) {
        this.result = result;
        this.error = error;
        this.conditions = conditions;
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
    public @NotNull List<RequirementCondition> getConditions() {
        return conditions;
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
    @Override
    public <R> ContextResult<R> map(Function<T, R> mapper) {
        if (this.isFailed()) {
            return (ContextResult<R>) this;
        }

        return ContextResult.ok(() -> mapper.apply(getSuccess()));
    }

    @Override
    public ContextResult<T> mapFailure(Function<Object, ContextResult<T>> mapper) {
        if (this.isFailed()) {
            return mapper.apply(error);
        }

        return this;
    }

    @ApiStatus.Experimental
    @Override
    public <R> ContextResult<R> flatMap(Function<T, ContextResult<R>> mapper) {
        if (this.isFailed()) {
            return ContextResult.error(error);
        }

        return mapper.apply(getSuccess());
    }

    @Override
    public ContextCompletedResult<T> whenSuccessful(Consumer<T> action) {
        if (this.isSuccessful()) {
            action.accept(this.getSuccess());
        }

        return this;
    }

    @Override
    public ContextCompletedResult<T> whenFailed(Consumer<Object> action) {
        if (this.isFailed()) {
            action.accept(this.error);
        }

        return this;
    }

    public static <T> ContextCompletedResult<T> ok(Supplier<T> supplier) {
        return new ContextCompletedResult<>(supplier, null, Collections.emptyList());
    }

    public static <T> ContextCompletedResult<T> error(Object error) {
        return new ContextCompletedResult<>(null, error, Collections.emptyList());
    }

    @ApiStatus.Experimental
    public static <T> ContextCompletedResult<T> conditional(Supplier<T> supplier, List<RequirementCondition> conditions) {
        return new ContextCompletedResult<>(supplier, null, Collections.unmodifiableList(conditions));
    }

    @Override
    public CompletableFuture<ContextCompletedResult<T>> asFuture() {
        return CompletableFuture.completedFuture(this);
    }

}
