package dev.rollczi.litecommands.argument;

import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.MatchResult;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

public interface SingleOrElseArgument<SENDER, A extends Annotation> extends Argument<SENDER, A> {

    @Override
    default MatchResult match(LiteInvocation invocation, ArgumentContext<A> context) {
        if (context.currentArgument() + 1 > invocation.arguments().length) {
            return orElse(invocation, context);
        }

        return match(invocation, context, invocation.arguments()[context.currentArgument()]);
    }

    MatchResult match(LiteInvocation invocation, ArgumentContext<A> context, String argument);

    MatchResult orElse(LiteInvocation invocation, ArgumentContext<A> context);

}
