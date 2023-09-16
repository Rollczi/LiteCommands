package dev.rollczi.litecommands.annotations.command.executor;

import dev.rollczi.litecommands.annotations.command.requirement.ParameterRequirementFactory;
import dev.rollczi.litecommands.annotations.command.requirement.NotAnnotatedParameterRequirementFactory;
import dev.rollczi.litecommands.annotations.command.requirement.RequirementAnnotation;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.command.requirement.Requirement;
import dev.rollczi.litecommands.reflect.LiteCommandsReflectException;
import dev.rollczi.litecommands.reflect.ReflectFormatUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MethodCommandExecutorFactory<SENDER> {

    private final Map<Class<?>, ParameterRequirementFactory<SENDER, ?>> resolvers = new java.util.HashMap<>();
    private NotAnnotatedParameterRequirementFactory<SENDER> defaultResolver = parameter -> {
        throw new IllegalArgumentException("No annotation found for parameter " + ReflectFormatUtil.parameter(parameter));
    };

    public CommandExecutor<SENDER, ?> create(CommandRoute<SENDER> parent, Object instance, Method method) {
        MethodDefinition<SENDER> definition = this.createDefinition(method);

        return new MethodCommandExecutor<>(parent, method, instance, definition);
    }

    public <A extends Annotation> void registerResolver(Class<A> type, ParameterRequirementFactory<SENDER, A> annotationResolver) {
        resolvers.put(type, annotationResolver);
    }

    public void defaultResolver(NotAnnotatedParameterRequirementFactory<SENDER> defaultResolver) {
        this.defaultResolver = defaultResolver;
    }

    private MethodDefinition<SENDER> createDefinition(Method method) {
        MethodDefinition<SENDER> definition = new MethodDefinition<>();

        int index = 0;
        for (Parameter parameter : method.getParameters()) {
            definition.putRequirement(index++, this.resolveParameter(parameter));
        }

        return definition;
    }

    private Requirement<SENDER, ?> resolveParameter(Parameter parameter) {
        Annotation[] annotations = parameter.getAnnotations();

        if (annotations.length == 0) {
            return defaultResolver.create(parameter);
        }

        for (Annotation annotation : annotations) {
            Optional<Requirement<SENDER, ?>> requirementOptional = this.resolveParameterWithAnnotation(parameter, annotation);

            if (requirementOptional.isPresent()) {
                return requirementOptional.get();
            }
        }

        throw new LiteCommandsReflectException(parameter.getDeclaringExecutable(), parameter, "Can not find resolver for parameter " + ReflectFormatUtil.parameter(parameter));
    }

    @SuppressWarnings("unchecked")
    private <A extends Annotation> Optional<Requirement<SENDER, ?>> resolveParameterWithAnnotation(Parameter parameter, A annotation) {
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
