package dev.rollczi.litecommands.annotations.processor;

import dev.rollczi.litecommands.command.builder.CommandBuilder;

import java.lang.annotation.Annotation;

class ClassInvoker<SENDER> implements AnnotationInvoker<SENDER> {

    private final Class<?> type;
    private final Object instance;
    private CommandBuilder<SENDER> commandBuilder;

    public ClassInvoker(Class<?> type, Object instance, CommandBuilder<SENDER> commandBuilder) {
        this.type = type;
        this.instance = instance;
        this.commandBuilder = commandBuilder;
    }

    @Override
    public <A extends Annotation> AnnotationInvoker<SENDER> onAnnotatedMetaHolder(Class<A> annotationType, AnnotationProcessor.MetaHolderListener<A> listener) {
        A annotation = type.getAnnotation(annotationType);

        if (annotation == null) {
            return this;
        }

        listener.call(instance, annotation, commandBuilder);
        return this;
    }

    @Override
    public <A extends Annotation> AnnotationInvoker<SENDER> onAnnotatedClass(Class<A> annotationType, AnnotationProcessor.ClassListener<SENDER, A> listener) {
        A annotation = type.getAnnotation(annotationType);

        if (annotation == null) {
            return this;
        }

        commandBuilder = listener.call(instance, annotation, commandBuilder);
        return this;
    }

    @Override
    public CommandBuilder<SENDER> getResult() {
        return commandBuilder;
    }

}
