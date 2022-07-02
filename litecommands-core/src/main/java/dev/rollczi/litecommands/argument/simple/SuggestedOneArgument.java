package dev.rollczi.litecommands.argument.simple;

import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.sugesstion.Suggestion;
import panda.std.Result;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

class SuggestedOneArgument<T> implements OneArgument<T> {

    private final BiFunction<LiteInvocation, String, Result<T, ?>> parse;
    private final Function<LiteInvocation, List<Suggestion>> suggest;
    private final BiFunction<LiteInvocation, Suggestion, Boolean> validate;

    SuggestedOneArgument(BiFunction<LiteInvocation, String, Result<T, ?>> parse, Function<LiteInvocation, List<Suggestion>> suggest, BiFunction<LiteInvocation, Suggestion, Boolean> validate) {
        this.parse = parse;
        this.suggest = suggest;
        this.validate = validate;
    }

    @Override
    public Result<T, ?> parse(LiteInvocation invocation, String argument) {
        return this.parse.apply(invocation, argument);
    }

    @Override
    public List<Suggestion> suggest(LiteInvocation invocation) {
        return this.suggest.apply(invocation);
    }

    @Override
    public boolean validate(LiteInvocation invocation, Suggestion suggestion) {
        return this.validate.apply(invocation, suggestion);
    }

}
