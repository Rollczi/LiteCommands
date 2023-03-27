package dev.rollczi.litecommands.argument;

import org.jetbrains.annotations.Nullable;
import panda.std.Option;

public class ArgumentResolverContext<E> {

    private final int lastResolvedRawArgument;
    private final @Nullable PreparedArgumentResult<E> lastArgumentResult;

    private ArgumentResolverContext(int lastResolvedRawArgument, @Nullable PreparedArgumentResult<E> lastArgumentResult) {
        this.lastResolvedRawArgument = lastResolvedRawArgument;
        this.lastArgumentResult = lastArgumentResult;
    }

    public int getLastResolvedRawArgument() {
        return this.lastResolvedRawArgument;
    }

    public Option<PreparedArgumentResult<E>> getLastArgumentResult() {
        return Option.of(this.lastArgumentResult);
    }

    public <T> ArgumentResolverContext<T> with(int consumed, PreparedArgumentResult<T> lastArgumentResult) {
        return new ArgumentResolverContext<>(this.lastResolvedRawArgument + consumed, lastArgumentResult);
    }

    public <T> ArgumentResolverContext<T> withFailure(PreparedArgumentResult<T> argumentResult) {
        return this.with(0, argumentResult);
    }

    public static <E> ArgumentResolverContext<E> create(int childIndex) {
        return new ArgumentResolverContext<>(childIndex, null);
    }

}
