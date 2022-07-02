package dev.rollczi.litecommands.argument;

import dev.rollczi.litecommands.command.MatchResult;

import java.lang.annotation.Annotation;
import java.util.List;

@Deprecated
public interface AnnotatedParameterState<SENDER, A extends Annotation> extends AnnotatedParameter<SENDER, A> {

    @Deprecated
    MatchResult matchResult();

    @Deprecated
    List<Object> result();

}
