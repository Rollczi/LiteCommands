package dev.rollczi.litecommands.modern.command.argument.invocation;

import org.jetbrains.annotations.Nullable;
import panda.std.Option;

public class ArgumentResolverContext {

    private final int lastResolvedRawArgument;
    private final @Nullable ArgumentResult<?> lastArgumentResult;

    private ArgumentResolverContext(int lastResolvedRawArgument, @Nullable ArgumentResult<?> lastArgumentResult) {
        this.lastResolvedRawArgument = lastResolvedRawArgument;
        this.lastArgumentResult = lastArgumentResult;
    }

    int getLastResolvedRawArgument() {
        return lastResolvedRawArgument;
    }

    public Option<ArgumentResult<?>> getLastArgumentResult() {
        return Option.of(lastArgumentResult);
    }

    ArgumentResolverContext with(int consumed, ArgumentResult<?> lastArgumentResult) {
        return new ArgumentResolverContext(this.lastResolvedRawArgument + consumed, lastArgumentResult);
    }

    ArgumentResolverContext withFailure() {
        return with(0, ArgumentResult.failure());
    }

    ArgumentResolverContext withFailure(ArgumentResult<?> argumentResult) {
        return with(0, argumentResult);
    }

    public static ArgumentResolverContext create() {
        return new ArgumentResolverContext(0, null);
    }
}
