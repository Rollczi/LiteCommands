package dev.rollczi.litecommands.annotations.command;

import java.lang.reflect.Parameter;

public interface ParameterWithoutAnnotationResolver<SENDER> {

    ParameterCommandRequirement<SENDER, ?> resolve(Parameter parameter);

}
