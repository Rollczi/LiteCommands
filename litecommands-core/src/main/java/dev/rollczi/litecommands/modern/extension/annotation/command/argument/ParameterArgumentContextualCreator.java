package dev.rollczi.litecommands.modern.extension.annotation.command.argument;

import dev.rollczi.litecommands.modern.command.contextual.warpped.WrappedExpectedContextualService;
import dev.rollczi.litecommands.modern.extension.annotation.command.contextual.ParameterContextual;
import dev.rollczi.litecommands.modern.shared.ParameterizedTypeUtils;
import dev.rollczi.litecommands.shared.ReflectFormat;
import panda.std.Option;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
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
    private <A extends Annotation, EXPECTED> ParameterArgumentContextual<A, EXPECTED> createParameterArgumentContextual(Parameter parameter, A annotation) {
        Method method = (Method) parameter.getDeclaringExecutable();
        Class<A> annotationType = (Class<A>) annotation.annotationType();
        Class<?> expectedType = parameter.getType();
        Class<?> expectedWrapperType = expectedType;

        if (this.wrappedExpectedContextualService.isWrapper(expectedType)) {
            Option<Class<?>> option = ParameterizedTypeUtils.extractFirstType(parameter);

            if (option.isEmpty()) {
                throw new IllegalArgumentException("Cannot extract expected type from parameter " + ReflectFormat.parameter(parameter));
            }

            expectedType = option.get();
            expectedWrapperType = Void.class;
        }

        return new ParameterArgumentContextual<>(parameter, method, annotation, annotationType, (Class<EXPECTED>)  expectedType, expectedWrapperType);
    }

}
