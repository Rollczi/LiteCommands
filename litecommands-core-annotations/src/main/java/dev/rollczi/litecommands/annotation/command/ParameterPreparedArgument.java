package dev.rollczi.litecommands.annotation.command;

import dev.rollczi.litecommands.argument.PreparedArgument;

import java.lang.reflect.Parameter;

public interface ParameterPreparedArgument<SENDER, EXPECTED> extends PreparedArgument<SENDER, EXPECTED> {

    Parameter getParameter();

    int getParameterIndex();

}
