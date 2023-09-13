package dev.rollczi.litecommands.annotations.processor;

import dev.rollczi.litecommands.command.builder.CommandBuilder;

import java.lang.annotation.Annotation;

public interface AnnotationInvoker<SENDER> {

    default <A extends Annotation>
    AnnotationInvoker<SENDER> on(Class<A> annotationType, AnnotationProcessor.Listener<A> listener) {
        return this;
    }

    default <A extends Annotation>
    AnnotationInvoker<SENDER> onStructure(Class<A> annotationType, AnnotationProcessor.StructureListener<SENDER, A> listener) {
        return this;
    }

    default <A extends Annotation>
    AnnotationInvoker<SENDER> onExecutorStructure(Class<A> annotationType, AnnotationProcessor.StructureExecutorListener<SENDER, A> listener) {
        return this;
    }

    default <A extends Annotation>
    AnnotationInvoker<SENDER> onRequirement(Class<A> annotationType, AnnotationProcessor.RequirementListener<SENDER, A> listener) {
        return this;
    }

    CommandBuilder<SENDER> getResult();

}
