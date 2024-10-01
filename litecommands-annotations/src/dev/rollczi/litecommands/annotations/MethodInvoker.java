package dev.rollczi.litecommands.annotations;

import dev.rollczi.litecommands.annotations.validator.method.MethodValidatorService;
import dev.rollczi.litecommands.command.CommandExecutorProvider;
import dev.rollczi.litecommands.command.builder.CommandBuilder;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.meta.MetaHolder;
import dev.rollczi.litecommands.reflect.LiteCommandsReflectInvocationException;
import dev.rollczi.litecommands.reflect.type.TypeToken;
import dev.rollczi.litecommands.requirement.Requirement;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;

class MethodInvoker<SENDER> implements AnnotationInvoker<SENDER>, MetaHolder {

    private final AnnotationProcessorService<SENDER> annotationProcessorService;
    private final Method method;
    private CommandBuilder<SENDER> commandBuilder;
    private final MethodDefinition methodDefinition;
    private final CommandExecutorProvider<SENDER> executorProvider;
    private final Meta meta = Meta.create();

    private boolean isExecutorStructure = false;

    public MethodInvoker(AnnotationProcessorService<SENDER> annotationProcessorService, MethodValidatorService<SENDER> validatorService, Object instance, Method method, CommandBuilder<SENDER> commandBuilder) {
        this.annotationProcessorService = annotationProcessorService;
        this.method = method;
        this.commandBuilder = commandBuilder;
        this.methodDefinition = new MethodDefinition(method);
        this.executorProvider = parent -> new MethodCommandExecutor<>(parent, method, instance, methodDefinition, meta, validatorService);
    }

    @Override
    public <A extends Annotation> AnnotationInvoker<SENDER> on(Class<A> annotationType, AnnotationProcessor.AnyListener<A> listener) {
        A annotation = method.getAnnotation(annotationType);

        if (annotation == null) {
            return this;
        }

        listener.call(annotation, this);
        return this;
    }

    @Override
    public <A extends Annotation> AnnotationInvoker<SENDER> onMethod(Class<A> annotationType, AnnotationProcessor.MethodListener<SENDER, A> listener) {
        A methodAnnotation = method.getAnnotation(annotationType);

        if (methodAnnotation == null) {
            return this;
        }

        listener.call(method, methodAnnotation, commandBuilder, executorProvider);
        isExecutorStructure = true;
        return this;
    }

    @Override
    public <A extends Annotation> AnnotationInvoker<SENDER> onParameter(Class<A> annotationType, AnnotationProcessor.ParameterListener<SENDER, A> listener) {
        for (int index = 0; index < method.getParameterCount(); index++) {
            Parameter parameter = method.getParameters()[index];
            A parameterAnnotation = parameter.getAnnotation(annotationType);

            if (parameterAnnotation == null) {
                continue;
            }

            if (methodDefinition.hasRequirement(index)) {
                continue;
            }

            Optional<Requirement<?>> requirementOptional = listener.call(parameter, createHolder(parameterAnnotation, parameter), commandBuilder);

            if (requirementOptional.isPresent()) {
                Requirement<?> requirement = requirementOptional.get();

                requirement.meta().put(Meta.REQUIREMENT_PARAMETER, parameter);
                methodDefinition.putRequirement(index, requirement);
                annotationProcessorService.process(new ParameterInvoker<>(commandBuilder, parameter, requirement));
            }
        }

        return this;
    }

    @Override
    public CommandBuilder<SENDER> getResult() {
        if (!isExecutorStructure) {
            return commandBuilder;
        }

        Parameter[] parameters = method.getParameters();

        for (int index = 0; index < parameters.length; index++) {
            if (methodDefinition.hasRequirement(index)) {
                continue;
            }

            Parameter parameter = parameters[index];
            throw new LiteCommandsReflectInvocationException(
                method,
                parameter,
                "Parameter '" + parameter.getName() + "' needs @Arg, @Flag, @Join, @Context or @Bind annotation for define requirement!"
            );
        }

        return commandBuilder;
    }

    private <A extends Annotation> AnnotationHolder<A, ?> createHolder(A annotation, Parameter parameter) {
        return AnnotationHolder.of(annotation, TypeToken.ofParameter(parameter), () -> parameter.getName());
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
