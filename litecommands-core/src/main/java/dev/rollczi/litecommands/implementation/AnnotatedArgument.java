package dev.rollczi.litecommands.implementation;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.ArgumentState;
import dev.rollczi.litecommands.command.Completion;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.MatchResult;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

final class AnnotatedArgument<A extends Annotation> implements ArgumentState {

    private final A annotationInstance;
    private final Class<?> type;
    private final Argument<A> argument;

    AnnotatedArgument(A annotationInstance, Class<?> type, Argument<A> argument) {
        this.annotationInstance = annotationInstance;
        this.type = type;
        this.argument = argument;
    }

    MatchResult match(LiteInvocation invocation, int route) {
        return argument.match(invocation, annotationInstance, route, route - 1);
    }

    List<Completion> complete(LiteInvocation invocation) {
        return argument.complete(invocation, annotationInstance);
    }

    Class<?> getType() {
        return type;
    }

    @Override
    public Argument<?> argument() {
        return this.argument;
    }

    @Override
    public Optional<String> name() {
        return this.argument.getName(annotationInstance);
    }

    @Override
    public Optional<String> scheme() {
        return this.argument.getScheme(annotationInstance);
    }

}
