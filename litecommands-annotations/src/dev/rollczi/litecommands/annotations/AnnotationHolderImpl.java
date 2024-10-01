package dev.rollczi.litecommands.annotations;

import dev.rollczi.litecommands.reflect.type.TypeToken;

import java.lang.annotation.Annotation;
import java.util.function.Supplier;

class AnnotationHolderImpl<A extends Annotation, T> implements AnnotationHolder<A, T> {

    private final A annotation;
    private final Supplier<String> name;
    private final TypeToken<T> typeToken;

    public AnnotationHolderImpl(A annotation, Supplier<String> name, TypeToken<T> typeToken) {
        this.annotation = annotation;
        this.name = name;
        this.typeToken = typeToken;
    }

    @Override
    public A getAnnotation() {
        return annotation;
    }

    @Override
    public String getName() {
        return name.get();
    }

    @Override
    public TypeToken<T> getType() {
        return typeToken;
    }

}
