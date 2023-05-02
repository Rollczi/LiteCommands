package dev.rollczi.litecommands.annotations.argument;

import dev.rollczi.litecommands.annotations.util.WrapperParameterUtil;
import dev.rollczi.litecommands.wrapper.WrappedExpectedService;
import dev.rollczi.litecommands.wrapper.WrapperFormat;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

class ArgParameterArgument<EXPECTED> extends ParameterArgument<Arg, EXPECTED> {

    protected ArgParameterArgument(Method method, Parameter parameter, int parameterIndex, Arg annotation, Class<Arg> annotationType, WrapperFormat<EXPECTED, ?> wrapperFormat) {
        super(method, parameter, parameterIndex, annotation, annotationType, wrapperFormat);
    }

    @Override
    public String getName() {
        String value = this.getAnnotation().value();

        if (!value.isEmpty()) {
            return value;
        }

        return super.getName();
    }

    @SuppressWarnings("unchecked")
    static <A extends Annotation, EXPECTED> ArgParameterArgument<EXPECTED> createArg(WrappedExpectedService wrappedExpectedService, Parameter parameter, Arg annotation) {
        Method method = (Method) parameter.getDeclaringExecutable();
        int index = Arrays.asList(method.getParameters()).indexOf(parameter);
        Class<Arg> annotationType = (Class<Arg>) annotation.annotationType();
        WrapperFormat<?, ?> wrapperFormat = WrapperParameterUtil.wrapperFormat(wrappedExpectedService, parameter);

        return new ArgParameterArgument<>(method, parameter, index, annotation, annotationType, (WrapperFormat<EXPECTED, ?>) wrapperFormat);
    }

}
