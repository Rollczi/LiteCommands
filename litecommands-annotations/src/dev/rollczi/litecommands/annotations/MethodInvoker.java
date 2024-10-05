package dev.rollczi.litecommands.annotations;

import dev.rollczi.litecommands.annotations.meta.MetaAnnotationKeys;
import dev.rollczi.litecommands.annotations.validator.method.MethodValidatorService;
import dev.rollczi.litecommands.command.CommandExecutorProvider;
import dev.rollczi.litecommands.command.builder.CommandBuilder;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.meta.MetaHolder;
import dev.rollczi.litecommands.reflect.LiteCommandsReflectInvocationException;
import dev.rollczi.litecommands.requirement.Requirement;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;
import java.util.function.BiFunction;

public class MethodInvoker<SENDER> implements AnnotationInvoker<SENDER>, MetaHolder {

    private final AnnotationProcessorService<SENDER> annotationProcessorService;
    private final Method method;
    private final CommandBuilder<SENDER> commandBuilder;
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

            Optional<Requirement<?>> requirementOptional = listener.call(parameter, parameterAnnotation, commandBuilder);

            registerRequirement(index, parameter, parameterAnnotation, requirementOptional);
        }

        return this;
    }

    @ApiStatus.Experimental
    public MethodInvoker<SENDER> mapParameter(BiFunction<Parameter, Annotation, Optional<Requirement<?>>> provider) {
        for (int index = 0; index < method.getParameterCount(); index++) {
            Parameter parameter = method.getParameters()[index];

            for (Annotation annotation : parameter.getAnnotations()) {
                Optional<Requirement<?>> requirementOptional = provider.apply(parameter, annotation);

                registerRequirement(index, parameter, annotation, requirementOptional);
            }
        }

        return this;
    }

    private void registerRequirement(int index, Parameter parameter, Annotation annotation, Optional<Requirement<?>> requirementOptional) {
        if (requirementOptional.isPresent()) {
            Requirement<?> requirement = requirementOptional.get();
            requirement.meta()
                .put(MetaAnnotationKeys.SOURCE_PARAMETER, parameter)
                .put(MetaAnnotationKeys.SOURCE_ANNOTATION, annotation)
                .put(MetaAnnotationKeys.SOURCE_ANNOTATED_ELEMENT, parameter);

            methodDefinition.putRequirement(index, requirement);
            annotationProcessorService.process(new ParameterInvoker<>(commandBuilder, parameter, requirement));
        }
    }

    @ApiStatus.Experimental
    public Method getMethod() {
        return method;
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

    @Override
    public Meta meta() {
        return meta;
    }

    @Override
    public @Nullable MetaHolder parentMeta() {
        return commandBuilder;
    }

}
