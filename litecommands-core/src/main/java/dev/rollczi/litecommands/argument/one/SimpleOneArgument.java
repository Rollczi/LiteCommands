package dev.rollczi.litecommands.argument.one;

import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.SingleArgument;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.MatchResult;
import dev.rollczi.litecommands.command.sugesstion.Suggestion;
import panda.std.Blank;
import panda.std.Result;

import java.lang.reflect.Parameter;
import java.util.List;

public class SimpleOneArgument<T> implements SingleArgument<Arg> {

    private final OneArgument<T> oneArgument;

    public SimpleOneArgument(OneArgument<T> oneArgument) {
        this.oneArgument = oneArgument;
    }

    @Override
    public MatchResult match(LiteInvocation invocation, Parameter parameter, Arg annotation, int currentRoute, int currentArgument, String argument) {
        if (currentArgument >= invocation.arguments().length) {
            return MatchResult.notMatched();
        }

        Result<T, Object> parsed = this.oneArgument.parse(invocation, invocation.arguments()[currentArgument]);

        if (parsed.isErr()) {
            Object error = parsed.getError();

            if (error instanceof Blank) {
                return MatchResult.notMatched();
            }

            return MatchResult.notMatched(error);
        }

        return MatchResult.matched(parsed.get(), 1);
    }

    @Override
    public List<Suggestion> suggestion(LiteInvocation invocation, Parameter parameter, Arg annotation) {
        return this.oneArgument.suggest(invocation);
    }

    @Override
    public boolean isOptional() {
        return false;
    }

    @Override
    public Class<?> getNativeClass() {
        return this.oneArgument.getClass();
    }

}
