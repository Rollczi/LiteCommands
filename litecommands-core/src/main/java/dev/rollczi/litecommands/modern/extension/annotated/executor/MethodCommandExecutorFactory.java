package dev.rollczi.litecommands.modern.extension.annotated.executor;

import dev.rollczi.litecommands.modern.command.contextual.warpped.WrappedExpectedContextualService;
import dev.rollczi.litecommands.shared.ReflectFormat;
import panda.std.Option;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class MethodCommandExecutorFactory {

    private final WrappedExpectedContextualService wrappedExpectedContextualService;

    public MethodCommandExecutorFactory(WrappedExpectedContextualService wrappedExpectedContextualService) {
        this.wrappedExpectedContextualService = wrappedExpectedContextualService;
    }

    private MethodCommandExecutor create(Object instance, Method method) {
        List<ParameterContextual<?>> expectedContextuals = new ArrayList<>();

        for (Parameter parameter : method.getParameters()) {
            List<ParameterContextual<?>> argumentContextuals = this.createParameterArgumentContextuals(parameter);

            if (!argumentContextuals.isEmpty()) {
                expectedContextuals.addAll(argumentContextuals);
                continue;
            }

            expectedContextuals.add(this.createParameterContextual(parameter));
        }

        return new MethodCommandExecutor(method, expectedContextuals, instance);
    }

    private List<ParameterContextual<?>> createParameterArgumentContextuals(Parameter parameter) {
        List<ParameterContextual<?>> expectedContextuals = new ArrayList<>();

        for (Annotation annotation : parameter.getAnnotations()) {
            if (!annotation.annotationType().isAnnotationPresent(ArgumentAnnotation.class)) {
                continue;
            }

            expectedContextuals.add(this.createParameterArgumentContextual(parameter, annotation));
        }

        return expectedContextuals;
    }

    @SuppressWarnings("unchecked")
    private <A extends Annotation, EXPECTED> ParameterArgumentContextual<A, EXPECTED> createParameterArgumentContextual(Parameter parameter, A annotation) {
        ParameterArgument<A> argument = new ParameterArgument<>();

        Method method = (Method) parameter.getDeclaringExecutable();
        Class<A> annotationType = (Class<A>) annotation.annotationType();
        Class<?> expectedType = parameter.getType();
        Class<?> expectedWrapperType = expectedType;

        if (this.wrappedExpectedContextualService.isWrapper(expectedType)) {
            Option<Class<?>> option = ParameterizedTypeUtils.extractType(parameter);

            if (option.isEmpty()) {
                throw new IllegalArgumentException("Cannot extract expected type from parameter " + ReflectFormat.parameter(parameter));
            }

            expectedType = option.get();
            expectedWrapperType = Void.class;
        }

        return new ParameterArgumentContextual<>(argument, parameter, method, annotation, annotationType, (Class<EXPECTED>) expectedType, expectedWrapperType);
    }

    private <EXPECTED> ParameterContextual<EXPECTED> createParameterContextual(Parameter parameter) {
        Method method = (Method) parameter.getDeclaringExecutable();
        Class<?> expectedType = parameter.getType();
        Class<?> expectedWrapperType = expectedType;

        if (this.wrappedExpectedContextualService.isWrapper(expectedType)) {
            Option<Class<?>> option = ParameterizedTypeUtils.extractType(parameter);

            if (option.isEmpty()) {
                throw new IllegalArgumentException("Cannot extract expected type from parameter " + ReflectFormat.parameter(parameter));
            }

            expectedType = option.get();
            expectedWrapperType = Void.class;
        }

        return new ParameterContextual<>(method, parameter, (Class<EXPECTED>) expectedType, expectedWrapperType);
    }

}
