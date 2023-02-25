package dev.rollczi.litecommands.modern.annotation.argument;

import dev.rollczi.litecommands.modern.annotation.contextual.ParameterContextual;
import dev.rollczi.litecommands.modern.contextual.warpped.WrappedExpectedContextualService;
import dev.rollczi.litecommands.modern.util.ParameterizedTypeUtil;
import dev.rollczi.litecommands.shared.ReflectFormat;
import panda.std.Option;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class ParameterArgumentContextualCreator implements Function<Parameter, List<ParameterContextual<?>>> {

    private final WrappedExpectedContextualService wrappedExpectedContextualService;

    public ParameterArgumentContextualCreator(WrappedExpectedContextualService wrappedExpectedContextualService) {
        this.wrappedExpectedContextualService = wrappedExpectedContextualService;
    }

    @Override
    public List<ParameterContextual<?>> apply(Parameter parameter) {
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
    private <A extends Annotation, EXPECTED> ParameterArgument<A, EXPECTED> createParameterArgumentContextual(Parameter parameter, A annotation) {
        Method method = (Method) parameter.getDeclaringExecutable();
        int index = Arrays.asList(method.getParameters()).indexOf(parameter);
        Class<A> annotationType = (Class<A>) annotation.annotationType();
        Class<?> expectedType = parameter.getType();
        Class<?> expectedWrapperType = Void.class;

        if (this.wrappedExpectedContextualService.isWrapper(expectedType)) {
            Option<Class<?>> option = ParameterizedTypeUtil.extractFirstType(parameter);

            if (option.isEmpty()) {
                throw new IllegalArgumentException("Cannot extract expected type from parameter " + ReflectFormat.parameter(parameter));
            }

            expectedWrapperType = expectedType;
            expectedType = option.get();
        }

        return new ParameterArgument<>(method, parameter, index, annotation, annotationType, (Class<EXPECTED>)  expectedType, expectedWrapperType);
    }

}
