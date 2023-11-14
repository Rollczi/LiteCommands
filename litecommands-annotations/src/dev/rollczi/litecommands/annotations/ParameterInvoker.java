package dev.rollczi.litecommands.annotations;

import dev.rollczi.litecommands.command.builder.CommandBuilder;
import dev.rollczi.litecommands.requirement.Requirement;
import dev.rollczi.litecommands.wrapper.WrapFormat;
import dev.rollczi.litecommands.wrapper.WrapperRegistry;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

class ParameterInvoker<SENDER> implements AnnotationInvoker<SENDER> {

    private final WrapperRegistry wrapperRegistry;
    private final CommandBuilder<SENDER> commandBuilder;
    private final Parameter parameter;
    private final Requirement<?> requirement;

    ParameterInvoker(WrapperRegistry wrapperRegistry, CommandBuilder<SENDER> commandBuilder, Parameter parameter, Requirement<?> requirement) {
        this.wrapperRegistry = wrapperRegistry;
        this.commandBuilder = commandBuilder;
        this.parameter = parameter;
        this.requirement = requirement;
    }

    @Override
    public <A extends Annotation> AnnotationInvoker<SENDER> on(Class<A> annotationType, AnnotationProcessor.Listener<A> listener) {
        A annotation = parameter.getAnnotation(annotationType);

        if (annotation == null) {
            return this;
        }

        listener.call(annotation, requirement);
        return this;
    }

    @Override
    public <A extends Annotation> AnnotationInvoker<SENDER> onRequirementMeta(Class<A> annotationType, AnnotationProcessor.RequirementMetaListener<SENDER, A> listener) {
        A annotation = parameter.getAnnotation(annotationType);

        if (annotation == null) {
            return this;
        }

        listener.call(createHolder(annotation, parameter), commandBuilder, requirement);
        return this;
    }

    private <A extends Annotation> AnnotationHolder<A, ?, ?> createHolder(A annotation, Parameter parameter) {
        WrapFormat<?, ?> format = MethodParameterUtil.wrapperFormat(wrapperRegistry, parameter);

        return AnnotationHolder.of(parameter.getAnnotations(), annotation, format, () -> parameter.getName());
    }

    @Override
    public CommandBuilder<SENDER> getResult() {
        return commandBuilder;
    }

}
