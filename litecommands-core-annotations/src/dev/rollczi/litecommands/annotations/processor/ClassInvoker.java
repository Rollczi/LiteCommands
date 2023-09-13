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
    public <A extends Annotation> AnnotationInvoker<SENDER> on(Class<A> annotationType, AnnotationProcessor.Listener<A> listener) {
        A annotation = type.getAnnotation(annotationType);

        if (annotation == null) {
            return this;
        }

        listener.call(annotation, commandBuilder);
        return this;
    }

    @Override
    public <A extends Annotation> AnnotationInvoker<SENDER> onStructure(Class<A> annotationType, AnnotationProcessor.StructureListener<SENDER, A> listener) {
        A annotation = type.getAnnotation(annotationType);

        if (annotation == null) {
            return this;
        }

        commandBuilder = listener.call(annotation, commandBuilder);
        return this;
    }

    @Override
    public CommandBuilder<SENDER> getResult() {
        return commandBuilder;
    }

}
