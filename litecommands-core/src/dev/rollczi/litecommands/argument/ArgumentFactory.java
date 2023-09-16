package dev.rollczi.litecommands.argument;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.annotation.AnnotationHolder;

public interface ArgumentFactory<A> {
    <T> Argument<T> create(AnnotationHolder<A, T, ?> holder);
}
