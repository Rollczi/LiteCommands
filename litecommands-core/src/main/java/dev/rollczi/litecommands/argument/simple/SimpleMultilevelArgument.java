package dev.rollczi.litecommands.argument.simple;

import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.ArgumentContext;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.MatchResult;
import dev.rollczi.litecommands.suggestion.Suggestion;
import panda.std.Blank;
import panda.std.Result;

import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;

public class SimpleMultilevelArgument<SENDER, T> implements Argument<SENDER, Arg> {

    private final MultilevelArgument<T> multilevel;

    public SimpleMultilevelArgument(MultilevelArgument<T> multilevel) {
        this.multilevel = multilevel;
    }

    @Override
    public MatchResult match(LiteInvocation invocation, ArgumentContext<Arg> context) {
        int currentArgument = context.currentArgument();
        List<String> arguments = Arrays.asList(invocation.arguments());

        if (currentArgument + multilevel.countMultilevel() > arguments.size()) {
            return MatchResult.notMatched();
        }

        List<String> argumentsToParse = arguments.subList(currentArgument, currentArgument + multilevel.countMultilevel());
        Result<T, ?> parsed = this.multilevel.parseMultilevel(invocation, argumentsToParse.toArray(new String[0]));

        if (parsed.isErr()) {
            Object error = parsed.getError();

            if (error instanceof Blank) {
                return MatchResult.notMatched();
            }

            return MatchResult.notMatched(error);
        }

        return MatchResult.matched(parsed.get(), multilevel.countMultilevel());
    }

    @Override
    public List<Suggestion> suggestion(LiteInvocation invocation, Parameter parameter, Arg annotation) {
        return this.multilevel.suggest(invocation);
    }

    @Override
    public boolean isOptional() {
        return false;
    }

    @Override
    public Class<?> getNativeClass() {
        return this.multilevel.getClass();
    }

    @Override
    public boolean validate(LiteInvocation invocation, Suggestion suggestion) {
        return this.multilevel.validate(invocation, suggestion);
    }
}
