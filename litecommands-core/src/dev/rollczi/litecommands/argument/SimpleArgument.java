package dev.rollczi.litecommands.argument;

import dev.rollczi.litecommands.annotation.AnnotationHolder;
import dev.rollczi.litecommands.wrapper.WrapFormat;

import java.lang.annotation.Annotation;

public class SimpleArgument<A extends Annotation, PARSED> implements Argument<PARSED> {

    private final AnnotationHolder<A, PARSED, ?> holder;

    public SimpleArgument(AnnotationHolder<A, PARSED, ?> holder) {
        this.holder = holder;
    }

    public A getAnnotation() {
        return holder.getAnnotation();
    }

    @Override
    public AnnotationHolder<?, PARSED, ?> getAnnotationHolder() {
        return holder;
    }

    @Override
    public WrapFormat<PARSED, ?> getWrapperFormat() {
        return holder.getFormat();
    }

    @Override
    public String getName() {
        return this.holder.getName();
    }

}
