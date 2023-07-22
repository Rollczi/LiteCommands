package dev.rollczi.litecommands.annotations.argument;

import dev.rollczi.litecommands.wrapper.WrapperRegistry;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

public interface ParameterArgumentFactory<A extends Annotation> {
    ParameterArgument<A, ?> create(WrapperRegistry wrapperRegistry, Parameter parameter, A annotation);
}
