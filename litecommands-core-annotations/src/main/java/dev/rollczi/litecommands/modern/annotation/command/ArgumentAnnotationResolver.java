package dev.rollczi.litecommands.modern.annotation.command;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

public interface ArgumentAnnotationResolver<SENDER, A extends Annotation> {

    ParameterPreparedArgument<SENDER, ?> resolve(Parameter parameter, A annotation);

}
