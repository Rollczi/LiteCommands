package dev.rollczi.litecommands.annotations;

import dev.rollczi.litecommands.wrapper.WrapFormat;

import java.lang.annotation.Annotation;
import java.util.function.Supplier;

public interface AnnotationHolder<A extends Annotation, PARSED, OUT> {

    A getAnnotation();

    String getName();

    WrapFormat<PARSED, OUT> getFormat();

    static <A extends Annotation, PARSED, OUT> AnnotationHolder<A, PARSED, OUT> of(A annotation, WrapFormat<PARSED, OUT> format, Supplier<String> nameSupplier) {
        return new AnnotationHolderImpl<>(annotation, nameSupplier, format);
    }

}
