package dev.rollczi.litecommands.modern.extension.annotation.command.contextual;

import dev.rollczi.litecommands.modern.command.contextual.warpped.WrappedExpectedContextualService;
import dev.rollczi.litecommands.modern.shared.ParameterizedTypeUtils;
import dev.rollczi.litecommands.shared.ReflectFormat;
import panda.std.Option;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class ParameterContextualCreator implements Function<Parameter, List<ParameterContextual<?>>> {

    private final WrappedExpectedContextualService wrappedExpectedContextualService;

    public ParameterContextualCreator(WrappedExpectedContextualService wrappedExpectedContextualService) {
        this.wrappedExpectedContextualService = wrappedExpectedContextualService;
    }

    @Override
    public List<ParameterContextual<?>> apply(Parameter parameter) {
        return Collections.singletonList(this.createParameterContextual(parameter));
    }

    @SuppressWarnings("unchecked")
    private <EXPECTED> ParameterContextual<EXPECTED> createParameterContextual(Parameter parameter) {
        Method method = (Method) parameter.getDeclaringExecutable();
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

        return new ParameterContextual<>(method, parameter, (Class<EXPECTED>) expectedType, expectedWrapperType);
    }

}
