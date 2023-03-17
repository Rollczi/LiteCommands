package dev.rollczi.litecommands.modern.annotation.command;

import dev.rollczi.litecommands.modern.util.ReflectFormatUtil;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParameterPreparedArgumentFactory<SENDER> {



    public <A extends Annotation> void register(Class<A> type, ArgumentAnnotationResolver<SENDER, A> annotationResolver) {
        resolvers.put(type, annotationResolver);
    }

    public void registerNoAnnotation(ArgumentNoAnnotationResolver noAnnotationResolver) {
        this.noAnnotationResolver = noAnnotationResolver;
    }

    public List<ParameterPreparedArgument<SENDER, ?>> resolve(Method method) {
        List<ParameterPreparedArgument<SENDER, ?>> preparedArguments = new ArrayList<>();

        for (Parameter parameter : method.getParameters()) {
            preparedArguments.addAll(resolve(parameter));
        }

        return preparedArguments;
    }

    private List<ParameterPreparedArgument<SENDER, ?>> resolve(Parameter parameter) {
        Annotation[] annotations = parameter.getAnnotations();

        if (annotations.length == 0) {
            if (noAnnotationResolver == null) {
                throw new IllegalStateException("Can not resolve " + ReflectFormatUtil.parameter(parameter));
            }

            ParameterPreparedArgument<SENDER, ?> argument = noAnnotationResolver.resolve(parameter);

            return Collections.singletonList(argument);
        }

        List<ParameterPreparedArgument<SENDER, ?>> preparedArguments = new ArrayList<>();

        for (Annotation annotation : annotations) {
            preparedArguments.add(resolve(parameter, annotation));
        }

        return preparedArguments;
    }

    @SuppressWarnings("unchecked")
    private <A extends Annotation> ParameterPreparedArgument<SENDER, ?> resolve(Parameter parameter, A annotation) {
        ArgumentAnnotationResolver<SENDER, A> resolver = (ArgumentAnnotationResolver<SENDER, A>) resolvers.get(annotation.annotationType());

        if (resolver == null) {
            throw new IllegalArgumentException("Can not resolve" + ReflectFormatUtil.parameter(parameter, annotation));
        }

        return resolver.resolve(parameter, annotation);
    }

}
