package dev.rollczi.litecommands.argument.option;

import dev.rollczi.litecommands.argument.one.OneArgument;
import dev.rollczi.litecommands.argument.SingleArgument;
import dev.rollczi.litecommands.command.Suggestion;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.MatchResult;
import panda.std.Option;

import java.util.Collections;
import java.util.List;

public class OptionArgument<T> implements SingleArgument<Opt> {

    private final Class<T> type;
    private final OneArgument<T> oneArgument;

    public OptionArgument(Class<T> type, OneArgument<T> oneArgument) {
        this.type = type;
        this.oneArgument = oneArgument;
    }

    @Override
    public MatchResult match(LiteInvocation invocation, Opt annotation, int currentRoute, int currentArgument, String argument) {
        if (!annotation.value().equals(type)) {
            return MatchResult.notMatched();
        }

        return MatchResult.matched(oneArgument.parse(invocation, argument).toOption(), 1);
    }

    @Override
    public List<Suggestion> complete(LiteInvocation invocation, Opt annotation) {
        return this.oneArgument.suggest(invocation);
    }

    @Override
    public Class<?> getNativeClass() {
        return this.oneArgument.getClass();
    }

    @Override
    public boolean isOptional() {
        return true;
    }

    @Override
    public List<Object> getDefault() {
        return Collections.singletonList(Option.none());
    }

}
