package dev.rollczi.litecommands.modern.annotation.contextual;

import dev.rollczi.litecommands.modern.contextual.ExpectedContextual;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Objects;

public class ParameterContextual<EXPECTED> implements ExpectedContextual<EXPECTED> {

    private final Method method;
    private final Parameter parameter;
    private final int parameterIndex;
    private final Class<EXPECTED> expectedType;
    private final Class<?> expectedWrapperType;

    protected ParameterContextual(Method method, Parameter parameter, int parameterIndex, Class<EXPECTED> expectedType, Class<?> expectedWrapperType) {
        this.method = method;
        this.parameter = parameter;
        this.parameterIndex = parameterIndex;
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

    public int getParameterIndex() {
        return parameterIndex;
    }

    public Method getMethod() {
        return this.method;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ParameterContextual)) {
            return false;
        }
        ParameterContextual<?> that = (ParameterContextual<?>) o;
        return this.method.equals(that.method) && this.parameter.equals(that.parameter) && this.expectedType.equals(that.expectedType) && this.expectedWrapperType.equals(that.expectedWrapperType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.method, this.parameter, this.expectedType, this.expectedWrapperType);
    }

}
