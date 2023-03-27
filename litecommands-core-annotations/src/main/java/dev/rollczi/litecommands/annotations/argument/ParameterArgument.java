package dev.rollczi.litecommands.annotations.argument;

import dev.rollczi.litecommands.annotations.util.WrapperParameterUtil;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.util.ReflectFormatUtil;
import dev.rollczi.litecommands.wrapper.WrappedExpectedService;
import dev.rollczi.litecommands.wrapper.WrapperFormat;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

public class ParameterArgument<A extends Annotation, EXPECTED> implements Argument<EXPECTED> {

    private final Method method;
    private final Parameter parameter;
    private final int parameterIndex;
    private final A annotation;
    private final Class<A> annotationType;

    private final WrapperFormat<EXPECTED, ?> wrapperFormat;

    protected ParameterArgument(Method method, Parameter parameter, int parameterIndex, A annotation, Class<A> annotationType, WrapperFormat<EXPECTED, ?> wrapperFormat) {
        this.method = method;
        this.parameter = parameter;
        this.parameterIndex = parameterIndex;
        this.wrapperFormat = wrapperFormat;
        this.annotation = annotation;
        this.annotationType = annotationType;
    }

    public Method getMethod() {
        return this.method;
    }

    public Parameter getParameter() {
        return this.parameter;
    }

    public int getParameterIndex() {
        return parameterIndex;
    }

    public A getAnnotation() {
        return annotation;
    }

    public Class<A> getAnnotationType() {
        return annotationType;
    }

    @Override
    public WrapperFormat<EXPECTED, ?> getWrapperFormat() {
        return wrapperFormat;
    }

    @Override
    public String getName() {
        return ReflectFormatUtil.parameter(this.getParameter(), this.annotation);
    }

    @SuppressWarnings("unchecked")
    public static <A extends Annotation, EXPECTED> ParameterArgument<A, EXPECTED> create(WrappedExpectedService wrappedExpectedService, Parameter parameter, A annotation) {
        Method method = (Method) parameter.getDeclaringExecutable();
        int index = Arrays.asList(method.getParameters()).indexOf(parameter);
        Class<A> annotationType = (Class<A>) annotation.annotationType();
        WrapperFormat<?, ?> wrapperFormat = WrapperParameterUtil.wrapperFormat(wrappedExpectedService, parameter);

        return new ParameterArgument<>(method, parameter, index, annotation, annotationType, (WrapperFormat<EXPECTED, ?>) wrapperFormat);
    }

}
