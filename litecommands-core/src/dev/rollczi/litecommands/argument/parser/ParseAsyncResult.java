package dev.rollczi.litecommands.argument.parser;

import dev.rollczi.litecommands.shared.FailedReason;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public class ParseAsyncResult<EXCEPTED> implements ParseResult<EXCEPTED> {

    private final CompletableFuture<? extends ParseResult<EXCEPTED>> future;

    public ParseAsyncResult(CompletableFuture<? extends ParseResult<EXCEPTED>> future) {
        this.future = future;
    }

    @Override
    public <R> ParseResult<R> map(Function<EXCEPTED, R> mapper) {
        return new ParseAsyncResult<>(future.thenApply(result -> result.map(mapper)));
    }

    @Override
    public ParseResult<EXCEPTED> mapFailure(Function<Object, ParseResult<EXCEPTED>> mapper) {
        return new ParseAsyncResult<>(future.thenApply(result -> result.mapFailure(mapper)));
    }

    @Override
    public <R> ParseResult<R> flatMap(Function<EXCEPTED, ParseResult<R>> mapper) {
        return new ParseAsyncResult<>(future.thenApply(result -> result.flatMap(mapper)));
    }

    @Override
    public ParseResult<EXCEPTED> whenSuccessful(Consumer<EXCEPTED> action) {
        this.future.thenAccept(result -> result.whenSuccessful(action));
        return this;
    }

    @Override
    public ParseResult<EXCEPTED> whenFailed(Consumer<FailedReason> action) {
        this.future.thenAccept(result -> result.whenFailed(action));
        return this;
    }

    @Override
    public CompletableFuture<ParseCompletedResult<EXCEPTED>> asFuture() {
        return future.thenCompose(exceptedParseResult -> exceptedParseResult.asFuture());
    }

    @ApiStatus.Experimental
    public static <T> ParseAsyncResult<T> of(CompletableFuture<ParseResult<T>> future) {
        return new ParseAsyncResult<>(future);
    }

}
