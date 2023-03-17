package dev.rollczi.litecommands.modern.annotation.argument;

import dev.rollczi.litecommands.modern.util.ReflectFormatUtil;
import dev.rollczi.litecommands.modern.wrapper.WrappedExpectedService;
import dev.rollczi.litecommands.modern.util.ParameterizedTypeUtil;
import panda.std.Option;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

public class ParameterArgumentFactory {

    private final WrappedExpectedService wrappedExpectedService;

    public ParameterArgumentFactory(WrappedExpectedService wrappedExpectedService) {
        this.wrappedExpectedService = wrappedExpectedService;
    }

    @SuppressWarnings("unchecked")
    public  <A extends Annotation, EXPECTED> ParameterArgument<A, EXPECTED> create(Parameter parameter, A annotation) {
        Method method = (Method) parameter.getDeclaringExecutable();
        int index = Arrays.asList(method.getParameters()).indexOf(parameter);
        Class<A> annotationType = (Class<A>) annotation.annotationType();
        Class<?> expectedType = parameter.getType();
        Class<?> expectedWrapperType = Void.class;

        if (this.wrappedExpectedService.isWrapper(expectedType)) {
            Option<Class<?>> option = ParameterizedTypeUtil.extractFirstType(parameter);

            if (option.isEmpty()) {
                throw new IllegalArgumentException("Cannot extract expected type from parameter " + ReflectFormatUtil.parameter(parameter));
            }

            expectedWrapperType = expectedType;
            expectedType = option.get();
        }

        return new ParameterArgument<>(method, parameter, index, annotation, annotationType, (Class<EXPECTED>)  expectedType, expectedWrapperType);
    }

}
