package dev.rollczi.litecommands.argument;

import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.MatchResult;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

@FunctionalInterface
public interface SingleArgument<SENDER, A extends Annotation> extends SingleOrElseArgument<SENDER, A> {

    @Override
    default MatchResult orElse(LiteInvocation invocation, ArgumentContext<A> context) {
        return MatchResult.notMatched();
    }

}
