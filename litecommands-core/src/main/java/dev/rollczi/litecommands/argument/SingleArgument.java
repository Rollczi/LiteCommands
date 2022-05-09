package dev.rollczi.litecommands.argument;

import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.MatchResult;

import java.lang.annotation.Annotation;

@FunctionalInterface
public interface SingleArgument<A extends Annotation> extends Argument<A> {

    @Override
    default MatchResult match(LiteInvocation invocation, A annotation, int currentRoute, int currentArgument) {
        if (currentArgument >= invocation.arguments().length) {
            return MatchResult.notMatched();
        }

        return match(invocation, annotation, currentRoute, currentArgument, invocation.arguments()[currentArgument]);
    }

    MatchResult match(LiteInvocation invocation, A annotation, int currentRoute, int currentArgument, String argument);

}
