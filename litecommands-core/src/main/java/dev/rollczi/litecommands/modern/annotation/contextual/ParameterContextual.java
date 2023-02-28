package dev.rollczi.litecommands.modern.annotation.contextual;

import dev.rollczi.litecommands.modern.wrapper.WrapperFormat;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class ParameterContextual<EXPECTED> {

    private final Method method;
    private final Parameter parameter;
    private final int parameterIndex;
    private final WrapperFormat<EXPECTED> wrapperFormat;

    protected ParameterContextual(Method method, Parameter parameter, int parameterIndex, Class<EXPECTED> expectedType, Class<?> expectedWrapperType) {
        this.method = method;
        this.parameter = parameter;
        this.parameterIndex = parameterIndex;
        this.wrapperFormat = new WrapperFormat<>(expectedType, expectedWrapperType);
    }

    public Parameter getParameter() {
        return this.parameter;
    }

    public int getParameterIndex() {
        return parameterIndex;
    }

    public WrapperFormat<EXPECTED> getWrapperFormat() {
        return wrapperFormat;
    }

    public Method getMethod() {
        return this.method;
    }

}
