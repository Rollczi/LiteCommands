package dev.rollczi.litecommands.implementation;

import dev.rollczi.litecommands.argument.AnnotatedParameterState;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.MatchResult;
import dev.rollczi.litecommands.command.sugesstion.Suggestion;
import dev.rollczi.litecommands.command.sugesstion.UniformSuggestionStack;
import panda.std.Lazy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.List;

final class AnnotatedParameterStateImpl<SENDER, A extends Annotation> extends AnnotatedParameterImpl<SENDER, A> implements AnnotatedParameterState<SENDER, A> {

    private final LiteInvocation invocation;
    private final Lazy<MatchResult> matchResult;
    private final Lazy<List<Suggestion>> suggestions;
    private final int route;

    AnnotatedParameterStateImpl(A annotationInstance, Parameter parameter, Argument<SENDER, A> argument, LiteInvocation invocation, int route) {
        super(annotationInstance, parameter, argument);
        this.invocation = invocation;
        this.route = route;
        this.matchResult = HandleUtil.handle(() -> this.match(invocation, route), ex -> MatchResult.notMatched(ex.getResult()));
        this.suggestions = HandleUtil.handle(() -> this.extractSuggestion(invocation), ex -> Collections.emptyList());
    }

    @Override
    public LiteInvocation invocation() {
        return invocation;
    }

    @Override
    public int route() {
        return route;
    }

    @Override
    public MatchResult matchResult() {
        return this.matchResult.get();
    }

    @Override
    public List<Object> result() {
        return this.matchResult.get().getResults();
    }

    @Override
    public UniformSuggestionStack suggest() {
        UniformSuggestionStack.empty()
                .with(Suggestion.multilevel("", ""))
                .with(Suggestion.multilevel("", ""))
                .with(Suggestion.multilevel("", ""));

        return UniformSuggestionStack.of(this.suggestions.get());
    }

}
