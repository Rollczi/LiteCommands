package dev.rollczi.litecommands.annotations.processor;

import dev.rollczi.litecommands.command.builder.CommandBuilder;

import java.lang.annotation.Annotation;

public interface AnnotationInvoker<SENDER> {

    default <A extends Annotation>
    AnnotationInvoker<SENDER> onAnnotatedMetaHolder(Class<A> annotationType, AnnotationProcessor.MetaHolderListener<A> listener) {
        return this;
    }

    default <A extends Annotation>
    AnnotationInvoker<SENDER> onAnnotatedClass(Class<A> annotationType, AnnotationProcessor.ClassListener<SENDER, A> listener) {
        return this;
    }

    default <A extends Annotation>
    AnnotationInvoker<SENDER> onAnnotatedMethod(Class<A> annotationType, AnnotationProcessor.MethodListener<SENDER, A> listener) {
        return this;
    }

    default <A extends Annotation>
    AnnotationInvoker<SENDER> onAnnotatedParameter(Class<A> annotationType, AnnotationProcessor.ParameterListener<SENDER, A> listener) {
        return this;
    }

    CommandBuilder<SENDER> getResult();

}
