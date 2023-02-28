package dev.rollczi.litecommands.modern.annotation.argument;

import dev.rollczi.litecommands.modern.annotation.contextual.ParameterContextual;
import dev.rollczi.litecommands.modern.argument.Argument;
import dev.rollczi.litecommands.modern.util.ReflectFormatUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Objects;

public class ParameterArgument<A extends Annotation, EXPECTED> extends ParameterContextual<EXPECTED> implements Argument<EXPECTED> {

    private final A annotation;
    private final Class<A> annotationType;

    protected ParameterArgument(Method method, Parameter parameter, int parameterIndex, A annotation, Class<A> annotationType, Class<EXPECTED> expectedType, Class<?> expectedWrapperType) {
        super(method, parameter, parameterIndex, expectedType, expectedWrapperType);
        this.annotation = annotation;
        this.annotationType = annotationType;
    }

    public A getAnnotation() {
        return annotation;
    }

    public Class<A> getAnnotationType() {
        return annotationType;
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

    @Override
    public String getName() {
        return ReflectFormatUtil.parameter(this.getParameter(), this.annotation);
    }

}
