package dev.rollczi.litecommands.context;

import dev.rollczi.litecommands.requirement.RequirementFutureResult;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class AsyncContextResult<T> implements ContextResult<T>, RequirementFutureResult<T> {

    private final CompletableFuture<? extends ContextResult<T>> future;

    public AsyncContextResult(CompletableFuture<ContextResult<T>> future) {
        this.future = future;
    }

    @Override
    public <R> ContextResult<R> map(Function<T, R> mapper) {
        return new AsyncContextResult<>(future.thenApply(result -> result.map(mapper)));
    }

    @Override
    public ContextResult<T> mapFailure(Function<Object, ContextResult<T>> mapper) {
        return new AsyncContextResult<>(future.thenApply(result -> result.mapFailure(mapper)));
    }

    @Override
    public <R> ContextResult<R> flatMap(Function<T, ContextResult<R>> mapper) {
        return new AsyncContextResult<>(future.thenApply(result -> result.flatMap(mapper)));
    }

    @Override
    public ContextResult<T> whenSuccessful(Consumer<T> action) {
        this.future.thenAccept(result -> result.whenSuccessful(action));
        return this;
    }

    @Override
    public ContextResult<T> whenFailed(Consumer<Object> action) {
        this.future.thenAccept(result -> result.whenFailed(action));
        return this;
    }

    @Override
    public CompletableFuture<ContextCompletedResult<T>> asFuture() {
        return future.thenCompose(exceptedParseResult -> exceptedParseResult.asFuture());
    }

    public static <T> AsyncContextResult<T> of(Supplier<T> supplier) {
        return new AsyncContextResult<>(CompletableFuture.completedFuture(ContextResult.ok(supplier)));
    }

}
