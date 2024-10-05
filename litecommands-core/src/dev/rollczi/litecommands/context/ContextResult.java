package dev.rollczi.litecommands.context;

import dev.rollczi.litecommands.requirement.RequirementCondition;
import dev.rollczi.litecommands.requirement.RequirementFutureResult;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Supplier;

public interface ContextResult<T> extends RequirementFutureResult<T> {

    @Override
    CompletableFuture<ContextCompletedResult<T>> asFuture();

    @ApiStatus.Experimental
    <R> ContextResult<R> map(Function<T, R> mapper);

    @ApiStatus.Experimental
    ContextResult<T> mapFailure(Function<Object, ContextResult<T>> mapper);

    @ApiStatus.Experimental
    <R> ContextResult<R> flatMap(Function<T, ContextResult<R>> mapper);

    @ApiStatus.Experimental
    ContextResult<T> whenSuccessful(Consumer<T> action);

    @ApiStatus.Experimental
    ContextResult<T> whenFailed(Consumer<Object> action);

    static <T> ContextCompletedResult<T> ok(Supplier<T> supplier) {
        return new ContextCompletedResult<>(supplier, null, Collections.emptyList());
    }

    static <T> ContextCompletedResult<T> error(Object error) {
        return new ContextCompletedResult<>(null, error, Collections.emptyList());
    }

    @ApiStatus.Experimental
    static <T> ContextResult<T> conditional(Supplier<T> supplier, List<RequirementCondition> conditions) {
        return new ContextCompletedResult<>(supplier, null, Collections.unmodifiableList(conditions));
    }

    @ApiStatus.Experimental
    static <T> AsyncContextResult<T> completableFuture(CompletableFuture<ContextResult<T>> future) {
        return new AsyncContextResult<>(future);
    }

    @ApiStatus.Experimental
    static <T, EXPECTED> AsyncContextResult<EXPECTED> completableFuture(CompletableFuture<T> future, Function<T, ? extends ContextResult<EXPECTED>> mapper) {
        return new AsyncContextResult<>(future.thenApply(mapper));
    }

    @ApiStatus.Experimental
    static <EXPECTED> AsyncContextResult<EXPECTED> async(Supplier<? extends ContextResult<EXPECTED>> supplier) {
        return new AsyncContextResult<>(CompletableFuture.supplyAsync(supplier));
    }

    @ApiStatus.Experimental
    static <EXPECTED> AsyncContextResult<EXPECTED> async(Supplier<? extends ContextResult<EXPECTED>> supplier, Executor executor) {
        return new AsyncContextResult<>(CompletableFuture.supplyAsync(supplier, executor));
    }


}
