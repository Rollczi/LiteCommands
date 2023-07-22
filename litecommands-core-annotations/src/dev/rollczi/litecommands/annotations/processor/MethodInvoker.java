package dev.rollczi.litecommands.annotations.processor;

import dev.rollczi.litecommands.command.builder.CommandBuilder;
import dev.rollczi.litecommands.command.builder.CommandBuilderExecutor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

class MethodInvoker<SENDER> implements AnnotationInvoker<SENDER> {

    private final Class<?> type;
    private final Object instance;
    private final Method method;
    private CommandBuilder<SENDER> commandBuilder;
    private final CommandBuilderExecutor<SENDER> executorBuilder;

    public MethodInvoker(Class<?> type, Object instance, Method method, CommandBuilder<SENDER> commandBuilder, CommandBuilderExecutor<SENDER> executorBuilder) {
        this.type = type;
        this.instance = instance;
        this.method = method;
        this.commandBuilder = commandBuilder;
        this.executorBuilder = executorBuilder;
    }

    @Override
    public <A extends Annotation> AnnotationInvoker<SENDER> onAnnotatedMetaHolder(Class<A> annotationType, AnnotationProcessor.MetaHolderListener<A> listener) {
        A annotation = method.getAnnotation(annotationType);

        if (annotation == null) {
            return this;
        }

        listener.call(instance, annotation, executorBuilder);
        return this;
    }

    @Override
    public <A extends Annotation> AnnotationInvoker<SENDER> onAnnotatedMethod(Class<A> annotationType, AnnotationProcessor.MethodListener<SENDER, A> listener) {
        A methodAnnotation = method.getAnnotation(annotationType);

        if (methodAnnotation == null) {
            return this;
        }

        commandBuilder = listener.call(instance, method, methodAnnotation, commandBuilder, executorBuilder);
        return this;
    }

    @Override
    public CommandBuilder<SENDER> getResult() {
        return commandBuilder;
    }


}
