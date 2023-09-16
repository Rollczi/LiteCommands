
package dev.rollczi.litecommands.annotations;

import dev.rollczi.litecommands.command.builder.CommandBuilder;
import dev.rollczi.litecommands.command.builder.CommandBuilderExecutor;
import dev.rollczi.litecommands.annotation.AnnotationHolder;
import dev.rollczi.litecommands.annotation.processor.AnnotationInvoker;
import dev.rollczi.litecommands.annotation.processor.AnnotationProcessor;
import dev.rollczi.litecommands.wrapper.WrapFormat;
import dev.rollczi.litecommands.wrapper.WrapperRegistry;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

class ParameterInvoker<SENDER> implements AnnotationInvoker<SENDER> {

    private final WrapperRegistry wrapperRegistry;
    private final Parameter parameter;
    private CommandBuilder<SENDER> commandBuilder;
    private final CommandBuilderExecutor<SENDER> executorBuilder;

    public ParameterInvoker(WrapperRegistry wrapperRegistry, Parameter parameter, CommandBuilder<SENDER> commandBuilder, CommandBuilderExecutor<SENDER> executorBuilder) {
        this.wrapperRegistry = wrapperRegistry;
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

        commandBuilder = listener.call(createHolder(parameterAnnotation, parameter), commandBuilder, executorBuilder);
        return this;
    }

    @Override
    public CommandBuilder<SENDER> getResult() {
        return commandBuilder;
    }

    private <A extends Annotation> AnnotationHolder<A, ?, ?> createHolder(A annotation, Parameter parameter) {
        WrapFormat<?, ?> format = MethodParameterUtil.wrapperFormat(wrapperRegistry, parameter);

        return AnnotationHolder.of(annotation, format, () -> parameter.getName());
    }

}
