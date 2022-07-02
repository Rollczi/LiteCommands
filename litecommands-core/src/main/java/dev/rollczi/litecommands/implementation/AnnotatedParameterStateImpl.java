package dev.rollczi.litecommands.implementation;

import dev.rollczi.litecommands.argument.AnnotatedParameterState;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.MatchResult;
import dev.rollczi.litecommands.sugesstion.Suggestion;
import dev.rollczi.litecommands.sugesstion.UniformSuggestionStack;
import panda.std.Lazy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.List;

@Deprecated
final class AnnotatedParameterStateImpl<SENDER, A extends Annotation> extends AnnotatedParameterImpl<SENDER, A> implements AnnotatedParameterState<SENDER, A> {

    private final Lazy<MatchResult> matchResult;

    AnnotatedParameterStateImpl(A annotationInstance, Parameter parameter, Argument<SENDER, A> argument, LiteInvocation invocation, int route) {
        super(annotationInstance, parameter, argument);
        this.matchResult = HandleUtil.handle(() -> this.match(invocation, route), ex -> MatchResult.notMatched(ex.getResult()));
    }

    @Override
    @Deprecated
    public MatchResult matchResult() {
        return this.matchResult.get();
    }

    @Override
    @Deprecated
    public List<Object> result() {
        return this.matchResult.get().getResults();
    }

}
