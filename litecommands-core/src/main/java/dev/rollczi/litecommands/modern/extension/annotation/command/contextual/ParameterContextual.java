package dev.rollczi.litecommands.modern.extension.annotation.command.contextual;

import dev.rollczi.litecommands.modern.command.contextual.ExpectedContextual;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class ParameterContextual<EXPECTED> implements ExpectedContextual<EXPECTED> {

    private final Method method;
    private final Parameter parameter;
    private final Class<EXPECTED> expectedType;
    private final Class<?> expectedWrapperType;

    ParameterContextual(Method method, Parameter parameter, Class<EXPECTED> expectedType, Class<?> expectedWrapperType) {
        this.method = method;
        this.parameter = parameter;
        this.expectedType = expectedType;
        this.expectedWrapperType = expectedWrapperType;
    }

    @Override
    public Class<EXPECTED> getExpectedType() {
        return this.expectedType;
    }

    @Override
    public Class<?> getExpectedWrapperType() {
        return this.expectedWrapperType;
    }

    public Parameter getParameter() {
        return this.parameter;
    }

    public Method getMethod() {
        return this.method;
    }

}
