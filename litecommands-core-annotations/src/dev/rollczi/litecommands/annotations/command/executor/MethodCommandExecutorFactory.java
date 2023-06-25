package dev.rollczi.litecommands.annotations.command.executor;

import dev.rollczi.litecommands.annotations.command.requirement.ParameterRequirement;
import dev.rollczi.litecommands.annotations.command.requirement.ParameterRequirementFactory;
import dev.rollczi.litecommands.annotations.command.requirement.NotAnnotatedParameterRequirementFactory;
import dev.rollczi.litecommands.annotations.command.requirement.RequirementAnnotation;
import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.reflect.LiteCommandsReflectException;
import dev.rollczi.litecommands.reflect.ReflectFormatUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MethodCommandExecutorFactory<SENDER> {

    private final Map<Class<?>, ParameterRequirementFactory<SENDER, ?>> resolvers = new java.util.HashMap<>();
    private NotAnnotatedParameterRequirementFactory<SENDER> defaultResolver = parameter -> {
        throw new IllegalArgumentException("No annotation found for parameter " + ReflectFormatUtil.parameter(parameter));
    };

    public CommandExecutor<SENDER, ?> create(Object instance, Method method) {
        List<ParameterRequirement<SENDER, ?>> arguments = this.resolveParameters(method);

        return new MethodCommandExecutor<>(method, instance, arguments);
    }

    public <A extends Annotation> void registerResolver(Class<A> type, ParameterRequirementFactory<SENDER, A> annotationResolver) {
        resolvers.put(type, annotationResolver);
    }

    public void defaultResolver(NotAnnotatedParameterRequirementFactory<SENDER> defaultResolver) {
        this.defaultResolver = defaultResolver;
    }

    private List<ParameterRequirement<SENDER, ?>> resolveParameters(Method method) {
        List<ParameterRequirement<SENDER, ?>> preparedArguments = new ArrayList<>();

        for (Parameter parameter : method.getParameters()) {
            preparedArguments.addAll(this.resolveParameter(parameter));
        }

        return preparedArguments;
    }

    private List<ParameterRequirement<SENDER, ?>> resolveParameter(Parameter parameter) {
        Annotation[] annotations = parameter.getAnnotations();

        if (annotations.length == 0) {
            ParameterRequirement<SENDER, ?> argument = defaultResolver.create(parameter);

            return Collections.singletonList(argument);
        }

        List<ParameterRequirement<SENDER, ?>> preparedArguments = new ArrayList<>();

        for (Annotation annotation : annotations) {
            Optional<ParameterRequirement<SENDER, ?>> requirementOptional = this.resolveParameterWithAnnotation(parameter, annotation);

            requirementOptional.ifPresent(requirement -> preparedArguments.add(requirement));
        }

        return preparedArguments;
    }

    @SuppressWarnings("unchecked")
    private <A extends Annotation> Optional<ParameterRequirement<SENDER, ?>> resolveParameterWithAnnotation(Parameter parameter, A annotation) {
        ParameterRequirementFactory<SENDER, A> resolver = (ParameterRequirementFactory<SENDER, A>) resolvers.get(annotation.annotationType());

        if (resolver == null) {
            if (!annotation.annotationType().isAnnotationPresent(RequirementAnnotation.class)) {
                return Optional.empty();
            }

            throw new LiteCommandsReflectException(parameter.getDeclaringExecutable(), parameter, "Can not find resolver for annotation @" + annotation.annotationType().getSimpleName());
        }

        return Optional.of(resolver.create(parameter, annotation));
    }

}
