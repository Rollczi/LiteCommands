package dev.rollczi.litecommands.implementation;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.ArgumentState;
import dev.rollczi.litecommands.command.Suggestion;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.MatchResult;
import panda.std.Option;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Optional;

final class AnnotatedArgument<A extends Annotation> implements ArgumentState {

    private final A annotationInstance;
    private final Parameter parameter;
    private final Argument<A> argument;

    AnnotatedArgument(A annotationInstance, Parameter parameter, Argument<A> argument) {
        this.annotationInstance = annotationInstance;
        this.parameter = parameter;
        this.argument = argument;
    }

    MatchResult match(LiteInvocation invocation, int route) {
        return argument.match(invocation, parameter, annotationInstance, route, route - 1);
    }

    List<Suggestion> complete(LiteInvocation invocation) {
        return argument.complete(invocation, parameter, annotationInstance);
    }

    Parameter getParameter() {
        return parameter;
    }

    @Override
    public Argument<?> argument() {
        return this.argument;
    }

    @Override
    public Option<String> name() {
        return this.argument.getName(parameter, annotationInstance);
    }

    @Override
    public Option<String> scheme() {
        return this.argument.getSchematic(annotationInstance);
    }

}
