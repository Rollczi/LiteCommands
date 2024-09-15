package dev.rollczi.litecommands.annotations;

import dev.rollczi.litecommands.command.builder.CommandBuilder;

import java.lang.annotation.Annotation;

public interface AnnotationInvoker<SENDER> {

    default <A extends Annotation>
    AnnotationInvoker<SENDER> on(Class<A> annotationType, AnnotationProcessor.AnyListener<A> listener) {
        return this;
    }

    default <A extends Annotation>
    AnnotationInvoker<SENDER> onClass(Class<A> annotationType, AnnotationProcessor.ClassListener<SENDER, A> listener) {
        return this;
    }

    default <A extends Annotation>
    AnnotationInvoker<SENDER> onMethod(Class<A> annotationType, AnnotationProcessor.MethodListener<SENDER, A> listener) {
        return this;
    }

    default <A extends Annotation>
    AnnotationInvoker<SENDER> onParameter(Class<A> annotationType, AnnotationProcessor.ParameterListener<SENDER, A> listener) {
        return this;
    }

    default <A extends Annotation>
    AnnotationInvoker<SENDER> onParameterRequirement(Class<A> annotationType, AnnotationProcessor.ParameterRequirementListener<SENDER, A> listener) {
        return this;
    }

    CommandBuilder<SENDER> getResult();

}
