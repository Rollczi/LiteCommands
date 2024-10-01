package dev.rollczi.litecommands.annotations;

import dev.rollczi.litecommands.reflect.type.TypeToken;

import java.lang.annotation.Annotation;
import java.util.function.Supplier;

public interface AnnotationHolder<A extends Annotation, T> {

    A getAnnotation();

    String getName();

    TypeToken<T> getType();

    static <A extends Annotation, T> AnnotationHolder<A, T> of(A annotation, TypeToken<T> format, Supplier<String> nameSupplier) {
        return new AnnotationHolderImpl<>(annotation, nameSupplier, format);
    }

}
