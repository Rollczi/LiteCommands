package dev.rollczi.litecommands.annotations.processor;

import dev.rollczi.litecommands.annotations.command.executor.MethodCommandExecutorFactory;
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
    private final MethodCommandExecutorFactory<SENDER> methodCommandExecutorFactory;

    public MethodInvoker(Class<?> type, Object instance, Method method, CommandBuilder<SENDER> commandBuilder, CommandBuilderExecutor<SENDER> executorBuilder, MethodCommandExecutorFactory<SENDER> methodCommandExecutorFactory) {
        this.type = type;
        this.instance = instance;
        this.method = method;
        this.commandBuilder = commandBuilder;
        this.executorBuilder = executorBuilder;
        this.methodCommandExecutorFactory = methodCommandExecutorFactory;
    }

    @Override
    public <A extends Annotation> AnnotationInvoker<SENDER> on(Class<A> annotationType, AnnotationProcessor.Listener<A> listener) {
        A annotation = method.getAnnotation(annotationType);

        if (annotation == null) {
            return this;
        }

        listener.call(annotation, executorBuilder);
        return this;
    }

    @Override
    public <A extends Annotation> AnnotationInvoker<SENDER> onExecutorStructure(Class<A> annotationType, AnnotationProcessor.StructureExecutorListener<SENDER, A> listener) {
        A methodAnnotation = method.getAnnotation(annotationType);

        if (methodAnnotation == null) {
            return this;
        }

        executorBuilder.setExecutorFactory(parent -> this.methodCommandExecutorFactory.create(parent, instance, method));
        commandBuilder = listener.call(methodAnnotation, commandBuilder, executorBuilder);
        return this;
    }

    @Override
    public CommandBuilder<SENDER> getResult() {
        return commandBuilder;
    }


}
