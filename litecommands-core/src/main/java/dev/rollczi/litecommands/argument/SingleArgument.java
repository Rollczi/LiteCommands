package dev.rollczi.litecommands.argument;

import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.MatchResult;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

@FunctionalInterface
public interface SingleArgument<A extends Annotation> extends Argument<A> {

    @Override
    default MatchResult match(LiteInvocation invocation, Parameter parameter, A annotation, int currentRoute, int currentArgument) {
        if (currentArgument >= invocation.arguments().length) {
            return MatchResult.notMatched();
        }

        return match(invocation, parameter, annotation, currentRoute, currentArgument, invocation.arguments()[currentArgument]);
    }

    MatchResult match(LiteInvocation invocation, Parameter parameter, A annotation, int currentRoute, int currentArgument, String argument);

}
