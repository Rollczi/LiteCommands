package dev.rollczi.litecommands.argument;

import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.MatchResult;

@FunctionalInterface
public interface OneArgument<T> extends Argument<Arg> {

    @Override
    default MatchResult match(LiteInvocation invocation, Arg arg, int currentRoute, int currentArgument) {
        if (currentArgument >= invocation.arguments().length) {
            return MatchResult.notMatched();
        }

        T parsed = parse(invocation, invocation.arguments()[currentArgument]);

        return MatchResult.matched(parsed, 1);
    }

    T parse(LiteInvocation invocation, String argument);

}
