package dev.rollczi.litecommands.modern.annotation.argument;

import dev.rollczi.litecommands.modern.annotation.contextual.ParameterContextual;
import dev.rollczi.litecommands.modern.argument.Argument;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Objects;

public class ParameterArgument<A extends Annotation, EXPECTED> extends ParameterContextual<EXPECTED> implements Argument<A, EXPECTED> {

    private final A annotation;
    private final Class<A> annotationType;

    protected ParameterArgument(Method method, Parameter parameter, int parameterIndex, A annotation, Class<A> annotationType, Class<EXPECTED> expectedType, Class<?> expectedWrapperType) {
        super(method, parameter, parameterIndex, expectedType, expectedWrapperType);
        this.annotation = annotation;
        this.annotationType = annotationType;
    }

    @Override
    public A getDeterminant() {
        return this.annotation;
    }

    @Override
    public Class<A> getDeterminantType() {
        return this.annotationType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ParameterArgument)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        ParameterArgument<?, ?> that = (ParameterArgument<?, ?>) o;
        return this.annotation.equals(that.annotation) && this.annotationType.equals(that.annotationType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.annotation, this.annotationType);
    }

}
