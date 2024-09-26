package dev.rollczi.litecommands.argument.parser;

import dev.rollczi.litecommands.requirement.ParseAsyncResult;
import dev.rollczi.litecommands.requirement.RequirementCondition;
import dev.rollczi.litecommands.requirement.RequirementFutureResult;
import dev.rollczi.litecommands.shared.FailedReason;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import org.jetbrains.annotations.ApiStatus;

public interface ParseResult<EXCEPTED> extends RequirementFutureResult<EXCEPTED> {

    static <PARSED> ParseCompletedResult<PARSED> success(PARSED parsed) {
        return new ParseCompletedResult<>(parsed, null, false, Collections.emptyList());
    }

    @SuppressWarnings("unchecked")
    static <T> ParseCompletedResult<T> successNull() {
        return (ParseCompletedResult<T>) ParseCompletedResult.NULL_SUCCESS;
    }

    static <EXPECTED> ParseCompletedResult<EXPECTED> failure(FailedReason failedReason) {
        return new ParseCompletedResult<>(null, failedReason, false, Collections.emptyList());
    }

    static <EXPECTED> ParseCompletedResult<EXPECTED> failure(Object failedReason) {
        return new ParseCompletedResult<>(null, FailedReason.of(failedReason), false, Collections.emptyList());
    }

    @Deprecated
    static <EXPECTED> ParseCompletedResult<EXPECTED> failure() {
        return new ParseCompletedResult<>(null, FailedReason.empty(), false, Collections.emptyList());
    }

    @ApiStatus.Experimental
    static <EXPECTED> ParseCompletedResult<EXPECTED> conditional(EXPECTED parsed, List<RequirementCondition> conditions) {
        return new ParseCompletedResult<>(parsed, null, false, Collections.unmodifiableList(conditions));
    }

    @ApiStatus.Experimental
    static <EXPECTED> ParseCompletedResult<EXPECTED> conditional(EXPECTED parsed, RequirementCondition... conditions) {
        return new ParseCompletedResult<>(parsed, null, false, Arrays.asList(conditions));
    }

    @ApiStatus.Experimental
    @ApiStatus.Internal
    static <EXPECTED> ParseAsyncResult<EXPECTED> completableFuture(CompletableFuture<ParseResult<EXPECTED>> future) {
        return new ParseAsyncResult<>(future);
    }

    @ApiStatus.Experimental
    static <T, EXPECTED> ParseAsyncResult<EXPECTED> completableFuture(CompletableFuture<T> future, Function<T, ParseResult<EXPECTED>> mapper) {
        return new ParseAsyncResult<>(future.thenApply(mapper));
    }

    @ApiStatus.Experimental
    @ApiStatus.Internal
    static <EXPECTED> ParseAsyncResult<EXPECTED> async(Supplier<ParseResult<EXPECTED>> supplier) {
        return new ParseAsyncResult<>(CompletableFuture.supplyAsync(supplier));
    }

    @ApiStatus.Experimental
    <R> ParseResult<R> map(Function<EXCEPTED, R> mapper);

    @ApiStatus.Experimental
    ParseResult<EXCEPTED> mapFailure(Function<Object, ParseResult<EXCEPTED>> mapper);

    @ApiStatus.Experimental
    <R> ParseResult<R> flatMap(Function<EXCEPTED, ParseResult<R>> mapper);

    @ApiStatus.Experimental
    ParseResult<EXCEPTED> whenSuccessful(Consumer<EXCEPTED> action);

    @ApiStatus.Experimental
    ParseResult<EXCEPTED> whenFailed(Consumer<FailedReason> action);

}
