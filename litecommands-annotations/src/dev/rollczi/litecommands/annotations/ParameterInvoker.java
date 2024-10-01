package dev.rollczi.litecommands.annotations;

import dev.rollczi.litecommands.command.builder.CommandBuilder;
import dev.rollczi.litecommands.reflect.type.TypeToken;
import dev.rollczi.litecommands.requirement.Requirement;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

class ParameterInvoker<SENDER> implements AnnotationInvoker<SENDER> {

    private final CommandBuilder<SENDER> commandBuilder;
    private final Parameter parameter;
    private final Requirement<?> requirement;

    ParameterInvoker(CommandBuilder<SENDER> commandBuilder, Parameter parameter, Requirement<?> requirement) {
        this.commandBuilder = commandBuilder;
        this.parameter = parameter;
        this.requirement = requirement;
    }

    @Override
    public <A extends Annotation> AnnotationInvoker<SENDER> on(Class<A> annotationType, AnnotationProcessor.AnyListener<A> listener) {
        A annotation = parameter.getAnnotation(annotationType);

        if (annotation == null) {
            return this;
        }

        listener.call(annotation, requirement);
        return this;
    }

    @Override
    public <A extends Annotation> AnnotationInvoker<SENDER> onParameterRequirement(Class<A> annotationType, AnnotationProcessor.ParameterRequirementListener<SENDER, A> listener) {
        A annotation = parameter.getAnnotation(annotationType);

        if (annotation == null) {
            return this;
        }

        listener.call(parameter, createHolder(annotation, parameter), commandBuilder, requirement);
        return this;
    }

    private <A extends Annotation> AnnotationHolder<A, ?> createHolder(A annotation, Parameter parameter) {
        return AnnotationHolder.of(annotation, TypeToken.ofParameter(parameter), () -> parameter.getName());
    }

    @Override
    public CommandBuilder<SENDER> getResult() {
        return commandBuilder;
    }

}
