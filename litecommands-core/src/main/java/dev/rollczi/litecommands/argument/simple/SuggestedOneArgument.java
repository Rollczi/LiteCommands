package dev.rollczi.litecommands.argument.simple;

import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.sugesstion.Suggestion;
import panda.std.Result;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

class SuggestedOneArgument<T> implements OneArgument<T> {

    private final BiFunction<LiteInvocation, String, Result<T, ?>> parse;
    private final Function<LiteInvocation, List<Suggestion>> suggest;

    SuggestedOneArgument(BiFunction<LiteInvocation, String, Result<T, ?>> parse, Function<LiteInvocation, List<Suggestion>> suggest) {
        this.parse = parse;
        this.suggest = suggest;
    }

    @Override
    public Result<T, ?> parse(LiteInvocation invocation, String argument) {
        return this.parse.apply(invocation, argument);
    }

    @Override
    public List<Suggestion> suggest(LiteInvocation invocation) {
        return this.suggest.apply(invocation);
    }

}
