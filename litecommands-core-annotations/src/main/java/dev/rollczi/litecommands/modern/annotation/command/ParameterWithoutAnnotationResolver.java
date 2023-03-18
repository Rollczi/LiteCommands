package dev.rollczi.litecommands.modern.annotation.command;

import java.lang.reflect.Parameter;

public interface ParameterWithoutAnnotationResolver<SENDER> {

    ParameterPreparedArgument<SENDER, ?> resolve(Parameter parameter);

}
