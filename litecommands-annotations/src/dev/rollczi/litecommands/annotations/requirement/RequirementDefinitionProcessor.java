package dev.rollczi.litecommands.annotations.requirement;

import dev.rollczi.litecommands.annotations.AnnotationInvoker;
import dev.rollczi.litecommands.annotations.AnnotationProcessor;
import dev.rollczi.litecommands.annotations.MethodInvoker;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.bind.Bind;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.flag.Flag;
import dev.rollczi.litecommands.annotations.varargs.Varargs;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.bind.BindRequirement;
import dev.rollczi.litecommands.context.ContextRequirement;
import dev.rollczi.litecommands.reflect.LiteCommandsReflectException;
import dev.rollczi.litecommands.reflect.type.TypeToken;
import dev.rollczi.litecommands.requirement.Requirement;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;

/**
 * Universal annotation processor for requirements such as {@link Argument}, {@link ContextRequirement} and {@link BindRequirement}.
 * It processes annotations with {@link RequirementDefinition} annotation. It creates requirements based on the type of the annotation.
 * Default annotations which are processed by this processor are {@link Arg}, {@link Varargs}, {@link Flag}, {@link Context}, {@link Bind} and more.
 */
public class RequirementDefinitionProcessor<SENDER> implements AnnotationProcessor<SENDER> {

    @Override
    public AnnotationInvoker<SENDER> process(AnnotationInvoker<SENDER> invoker) {
        if (!(invoker instanceof MethodInvoker)) {
            return invoker;
        }

        MethodInvoker<SENDER> methodInvoker = (MethodInvoker<SENDER>) invoker;

        return methodInvoker.mapParameter((parameter, annotation) -> {
            RequirementDefinition definition = annotation.annotationType().getAnnotation(RequirementDefinition.class);

            if (definition == null) {
                return Optional.empty();
            }

            return Optional.of(this.createRequirement(definition, parameter, annotation));
        });
    }

    private Requirement<?> createRequirement(RequirementDefinition definition, Parameter parameter, Annotation annotation) {
        String name = this.getName(definition, parameter, annotation);
        TypeToken<?> type = TypeToken.ofParameter(parameter);

        switch (definition.type()) {
            case ARGUMENT: return Argument.of(name, type);
            case CONTEXT: return ContextRequirement.of(() -> name, type);
            case BIND: return BindRequirement.of(() -> name, type);
        }

        throw new IllegalArgumentException("Unknown requirement type: " + definition.type());
    }

    private String getName(RequirementDefinition definition, Parameter parameter, Annotation annotation) {
        for (String attributeName : definition.nameProviders()) {
            try {
                Method attributeMethod = annotation.annotationType().getDeclaredMethod(attributeName);
                Object object = attributeMethod.invoke(annotation);

                if (!(object instanceof String)) {
                    throw new LiteCommandsReflectException("Attribute " + attributeName + " in annotation " + annotation.annotationType().getSimpleName() + " must return a String");
                }

                String nameCandidate = (String) object;

                if (!nameCandidate.isEmpty()) {
                    return nameCandidate;
                }
            }
            catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException exception) {
                throw new LiteCommandsReflectException("Cannot find method " + attributeName + " in annotation " + annotation.annotationType().getSimpleName(), exception);
            }
        }

        return parameter.getName();
    }

}
