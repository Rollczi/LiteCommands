package dev.rollczi.litecommands.argument.basictype;

import dev.rollczi.litecommands.argument.simple.OneArgument;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.suggestion.Suggestion;
import panda.std.Blank;
import panda.std.Result;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class AbstractBasicTypeArgument<T> implements OneArgument<T> {

    private final Function<String, T> parser;
    private final Supplier<String[]> suggestions;

    protected AbstractBasicTypeArgument(Function<String, T> parser, Supplier<String[]> suggestions) {
        this.parser = parser;
        this.suggestions = suggestions;
    }

    @Override
    public Result<T, Blank> parse(LiteInvocation invocation, String argument) {
        return TypeUtils.parse(() -> this.parser.apply(argument));
    }

    @Override
    public List<Suggestion> suggest(LiteInvocation invocation) {
        return TypeUtils.suggestion(this.parser, invocation, this.suggestions.get());
    }

    @Override
    public boolean validate(LiteInvocation invocation, Suggestion suggestion) {
        return TypeUtils.validate(this.parser, suggestion);
    }

}
