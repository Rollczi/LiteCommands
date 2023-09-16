package dev.rollczi.litecommands.annotations;

import dev.rollczi.litecommands.command.builder.CommandBuilder;
import dev.rollczi.litecommands.command.builder.CommandBuilderExecutor;
import dev.rollczi.litecommands.annotation.processor.AnnotationInvoker;
import dev.rollczi.litecommands.annotation.processor.AnnotationProcessor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

class MethodInvoker<SENDER> implements AnnotationInvoker<SENDER> {

    private final Object instance;
    private final Method method;
    private CommandBuilder<SENDER> commandBuilder;
    private final CommandBuilderExecutor<SENDER> executorBuilder;

    public MethodInvoker(Object instance, Method method, CommandBuilder<SENDER> commandBuilder, CommandBuilderExecutor<SENDER> executorBuilder) {
        this.instance = instance;
        this.method = method;
        this.commandBuilder = commandBuilder;
        this.executorBuilder = executorBuilder;
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

        executorBuilder.setExecutorFactory((parent, requirements) -> {
            MethodDefinition<SENDER> definition = MethodDefinition.createDefinition(method, requirements);

            return new MethodCommandExecutor<>(parent,  method, instance, definition);
        });

        commandBuilder = listener.call(methodAnnotation, commandBuilder, executorBuilder);
        return this;
    }

    @Override
    public CommandBuilder<SENDER> getResult() {
        return commandBuilder;
    }


}
