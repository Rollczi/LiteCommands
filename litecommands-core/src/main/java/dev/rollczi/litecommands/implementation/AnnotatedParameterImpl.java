package dev.rollczi.litecommands.implementation;

import dev.rollczi.litecommands.argument.AnnotatedParameter;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.AnnotatedParameterState;
import dev.rollczi.litecommands.argument.ArgumentContext;
import dev.rollczi.litecommands.command.sugesstion.Suggester;
import dev.rollczi.litecommands.command.sugesstion.Suggestion;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.MatchResult;
import dev.rollczi.litecommands.command.sugesstion.TwinSuggestionStack;
import panda.std.Option;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.List;

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
    public Option<String> schematic() {
        return this.argument.getSchematic(annotationInstance);
    }

    @Override
    public Suggester toSuggester(LiteInvocation invocation) {
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
        public TwinSuggestionStack suggest() {
            return TwinSuggestionStack.of(annotatedParameter.extractSuggestion(invocation));
        }
    }

}
