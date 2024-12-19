package dev.rollczi.litecommands.unit;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.parser.Parser;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.invocation.Invocation;

public final class Parsers {

    public static <S, T> Parser<S, T> of(T result) {
        return new ArgumentResolver<S, T>() {
            @Override
            protected ParseResult<T> parse(Invocation<S> invocation, Argument<T> context, String argument) {
                return ParseResult.success(result);
            }
        };
    }

}
