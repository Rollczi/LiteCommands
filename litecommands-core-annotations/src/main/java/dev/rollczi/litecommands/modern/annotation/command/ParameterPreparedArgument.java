package dev.rollczi.litecommands.modern.annotation.command;

import dev.rollczi.litecommands.modern.argument.PreparedArgument;

import java.lang.reflect.Parameter;

public interface ParameterPreparedArgument<SENDER, EXPECTED> extends PreparedArgument<SENDER, EXPECTED> {

    Parameter getParameter();

    int getParameterIndex();

}
