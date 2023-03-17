package dev.rollczi.litecommands.modern.annotation.command;

import java.lang.reflect.Parameter;

public interface ArgumentNoAnnotationResolver {

    <SENDER> ParameterPreparedArgument<SENDER, ?> resolve(Parameter parameter);

}
