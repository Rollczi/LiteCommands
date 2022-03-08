package dev.rollczi.litecommands.bind;

import dev.rollczi.litecommands.LiteInvocation;

import java.lang.annotation.Annotation;

@FunctionalInterface
public interface AnnotatedParameter<A extends Annotation> {

    Object apply(LiteInvocation invocation);

}
