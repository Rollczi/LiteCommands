package dev.rollczi.litecommands.annotations;

import dev.rollczi.litecommands.command.CommandExecutorProvider;
import dev.rollczi.litecommands.command.builder.CommandBuilder;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.meta.MetaHolder;
import dev.rollczi.litecommands.requirement.Requirement;
import dev.rollczi.litecommands.wrapper.WrapFormat;
import dev.rollczi.litecommands.wrapper.WrapperRegistry;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;

class MethodInvoker<SENDER> implements AnnotationInvoker<SENDER>, MetaHolder {

    private final AnnotationProcessorService<SENDER> annotationProcessorService;
    private final WrapperRegistry wrapperRegistry;
    private final Method method;
    private CommandBuilder<SENDER> commandBuilder;
    private final MethodDefinition methodDefinition;
    private final CommandExecutorProvider<SENDER> executorProvider;
    private final Meta meta = Meta.create();

    public MethodInvoker(AnnotationProcessorService<SENDER> annotationProcessorService, WrapperRegistry wrapperRegistry, Object instance, Method method, CommandBuilder<SENDER> commandBuilder) {
        this.annotationProcessorService = annotationProcessorService;
        this.wrapperRegistry = wrapperRegistry;
        this.method = method;
        this.commandBuilder = commandBuilder;
        this.methodDefinition = new MethodDefinition(method);
        this.executorProvider = parent -> new MethodCommandExecutor<>(parent, method, instance, methodDefinition, meta);
    }

    @Override
    public <A extends Annotation> AnnotationInvoker<SENDER> on(Class<A> annotationType, AnnotationProcessor.Listener<A> listener) {
        A annotation = method.getAnnotation(annotationType);

        if (annotation == null) {
            return this;
        }

        listener.call(annotation, this);
        return this;
    }

    @Override
    public <A extends Annotation> AnnotationInvoker<SENDER> onExecutorStructure(Class<A> annotationType, AnnotationProcessor.StructureExecutorListener<SENDER, A> listener) {
        A methodAnnotation = method.getAnnotation(annotationType);

        if (methodAnnotation == null) {
            return this;
        }

        commandBuilder = listener.call(methodAnnotation, commandBuilder, executorProvider);
        return this;
    }

    @Override
    public <A extends Annotation> AnnotationInvoker<SENDER> onRequirement(Class<A> annotationType, AnnotationProcessor.RequirementListener<SENDER, A> listener) {
        for (int index = 0; index < method.getParameterCount(); index++) {
            Parameter parameter = method.getParameters()[index];
            A parameterAnnotation = parameter.getAnnotation(annotationType);

            if (parameterAnnotation == null) {
                continue;
            }

            Optional<Requirement<?>> requirementOptional = listener.call(createHolder(parameterAnnotation, parameter), commandBuilder);

            if (requirementOptional.isPresent()) {
                Requirement<?> requirement = requirementOptional.get();

                methodDefinition.putRequirement(index, requirement);
                annotationProcessorService.process(new ParameterInvoker<>(wrapperRegistry, commandBuilder, parameter, requirement));
            }
        }

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

    @Override
    public Meta meta() {
        return meta;
    }

    @Override
    public @Nullable MetaHolder parentMeta() {
        return commandBuilder;
    }

}
