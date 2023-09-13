
package dev.rollczi.litecommands.annotations.processor;

import dev.rollczi.litecommands.command.builder.CommandBuilder;
import dev.rollczi.litecommands.command.builder.CommandBuilderExecutor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

class ParameterInvoker<SENDER> implements AnnotationInvoker<SENDER> {

    private final Object instance;
    private final Method method;
    private final Parameter parameter;
    private CommandBuilder<SENDER> commandBuilder;
    private final CommandBuilderExecutor<SENDER> executorBuilder;

    public ParameterInvoker(Object instance, Method method, Parameter parameter, CommandBuilder<SENDER> commandBuilder, CommandBuilderExecutor<SENDER> executorBuilder) {
        this.instance = instance;
        this.method = method;
        this.parameter = parameter;
        this.commandBuilder = commandBuilder;
        this.executorBuilder = executorBuilder;
    }

    @Override
    public <A extends Annotation> AnnotationInvoker<SENDER> onRequirement(Class<A> annotationType, AnnotationProcessor.RequirementListener<SENDER, A> listener) {
        A parameterAnnotation = parameter.getAnnotation(annotationType);

        if (parameterAnnotation == null) {
            return this;
        }

        commandBuilder = listener.call(parameterAnnotation, commandBuilder, executorBuilder);
        return this;
    }

    @Override
    public CommandBuilder<SENDER> getResult() {
        return commandBuilder;
    }

}
