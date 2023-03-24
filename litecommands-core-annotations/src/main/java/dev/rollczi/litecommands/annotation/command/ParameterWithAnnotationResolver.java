package dev.rollczi.litecommands.annotation.command;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

public interface ParameterWithAnnotationResolver<SENDER, A extends Annotation> {

    ParameterPreparedArgument<SENDER, ?> resolve(Parameter parameter, A annotation);

}
