package dev.rollczi.litecommands.annotations.command;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

public interface ParameterWithAnnotationResolver<SENDER, A extends Annotation> {

    ParameterCommandRequirement<SENDER, ?> resolve(Parameter parameter, A annotation);

}
