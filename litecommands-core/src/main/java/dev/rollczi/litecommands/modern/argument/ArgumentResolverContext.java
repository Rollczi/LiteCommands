package dev.rollczi.litecommands.modern.argument;

import org.jetbrains.annotations.Nullable;
import panda.std.Option;

public class ArgumentResolverContext<E> {

    private final int lastResolvedRawArgument;
    private final @Nullable ArgumentResult<E> lastArgumentResult;

    private ArgumentResolverContext(int lastResolvedRawArgument, @Nullable ArgumentResult<E> lastArgumentResult) {
        this.lastResolvedRawArgument = lastResolvedRawArgument;
        this.lastArgumentResult = lastArgumentResult;
    }

    int getLastResolvedRawArgument() {
        return this.lastResolvedRawArgument;
    }

    public Option<ArgumentResult<E>> getLastArgumentResult() {
        return Option.of(this.lastArgumentResult);
    }

    <T> ArgumentResolverContext<T> with(int consumed, ArgumentResult<T> lastArgumentResult) {
        return new ArgumentResolverContext<>(this.lastResolvedRawArgument + consumed, lastArgumentResult);
    }

    <T> ArgumentResolverContext<T> withFailure(ArgumentResult<T> argumentResult) {
        return this.with(0, argumentResult);
    }

    public static <E> ArgumentResolverContext<E> create(int childIndex) {
        return new ArgumentResolverContext<>(childIndex , null);
    }
}
