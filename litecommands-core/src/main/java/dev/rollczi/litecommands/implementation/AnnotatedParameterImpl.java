package dev.rollczi.litecommands.implementation;

import dev.rollczi.litecommands.argument.AnnotatedParameter;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.AnnotatedParameterState;
import dev.rollczi.litecommands.argument.ArgumentContext;
import dev.rollczi.litecommands.sugesstion.Suggest;
import dev.rollczi.litecommands.sugesstion.Suggester;
import dev.rollczi.litecommands.sugesstion.Suggestion;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.MatchResult;
import dev.rollczi.litecommands.sugesstion.UniformSuggestionStack;
import panda.std.Option;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

class AnnotatedParameterImpl<SENDER, A extends Annotation> implements AnnotatedParameter<SENDER, A> {

    private final A annotationInstance;
    private final Parameter parameter;
    private final Argument<SENDER, A> argument;

    AnnotatedParameterImpl(A annotationInstance, Parameter parameter, Argument<SENDER, A> argument) {
        this.annotationInstance = annotationInstance;
        this.parameter = parameter;
        this.argument = argument;
    }

    MatchResult match(LiteInvocation invocation, int route) {
        return argument.match(invocation, new ArgumentContext<>(parameter, annotationInstance, route, route - 1));
    }

    List<Suggestion> extractSuggestion(LiteInvocation invocation) {
        return argument.suggestion(invocation, parameter, annotationInstance);
    }

    Parameter getParameter() {
        return parameter;
    }

    @Deprecated
    AnnotatedParameterState<SENDER, A> createState(LiteInvocation invocation, int route) {
        return new AnnotatedParameterStateImpl<>(annotationInstance, parameter, argument, invocation, route);
    }

    @Override
    public A annotation() {
        return annotationInstance;
    }

    @Override
    public Argument<SENDER, A> argument() {
        return this.argument;
    }

    @Override
    public Option<String> name() {
        return this.argument.getName(parameter, annotationInstance);
    }

    @Override
    public List<Suggestion> staticSuggestions() {
        Suggest suggest = parameter.getAnnotation(Suggest.class);

        if (suggest == null) {
            return Collections.emptyList();
        }

        return Arrays.stream(suggest.value())
                .map(Suggestion::of)
                .collect(Collectors.toList());
    }

    @Override
    public Option<String> schematic() {
        return this.argument.getSchematic(parameter, annotationInstance);
    }

    @Override
    public Suggester toSuggester(LiteInvocation invocation, int route) {
        return new SimpleSuggester<>(this, invocation);
    }

    private static final class SimpleSuggester<SENDER> implements Suggester {

        private final AnnotatedParameterImpl<SENDER, ?> annotatedParameter;
        private final LiteInvocation invocation;

        private SimpleSuggester(AnnotatedParameterImpl<SENDER, ?> annotatedParameter, LiteInvocation invocation) {
            this.annotatedParameter = annotatedParameter;
            this.invocation = invocation;
        }

        @Override
        public boolean validate(Suggestion suggestion) {
            return this.annotatedParameter.argument().validate(invocation, suggestion);
        }

        @Override
        public UniformSuggestionStack suggestion() {
            return UniformSuggestionStack.of(annotatedParameter.extractSuggestion(invocation))
                    .with(annotatedParameter.staticSuggestions());
        }
    }

}
