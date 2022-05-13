package dev.rollczi.litecommands.argument;

import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.MatchResult;
import dev.rollczi.litecommands.command.sugesstion.Suggester;
import dev.rollczi.litecommands.command.sugesstion.Suggestion;

import java.lang.annotation.Annotation;
import java.util.List;

public interface AnnotatedParameterState<A extends Annotation> extends AnnotatedParameter<A>, Suggester {

    LiteInvocation invocation();

    int route();

    MatchResult matchResult();

    List<Object> result();

    @Override
    List<Suggestion> suggestions();

}
