package dev.rollczi.litecommands.annotation;

import dev.rollczi.litecommands.argument.Argument;

import java.lang.annotation.Annotation;

public interface ArgumentFactory<A extends Annotation> {
    <T> Argument<T> create(AnnotationHolder<A, T, ?> holder);
}
