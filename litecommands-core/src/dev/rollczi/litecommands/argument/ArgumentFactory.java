package dev.rollczi.litecommands.argument;

import dev.rollczi.litecommands.annotation.AnnotationHolder;

import java.lang.annotation.Annotation;

public interface ArgumentFactory<A extends Annotation> {
    <T> Argument<T> create(AnnotationHolder<A, T, ?> holder);
}
