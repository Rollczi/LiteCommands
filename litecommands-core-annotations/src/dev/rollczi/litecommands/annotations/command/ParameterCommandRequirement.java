package dev.rollczi.litecommands.annotations.command;

import dev.rollczi.litecommands.command.requirements.CommandRequirement;

import java.lang.reflect.Parameter;

public interface ParameterCommandRequirement<SENDER, RESULT> extends CommandRequirement<SENDER, RESULT> {

    Parameter getParameter();

    int getParameterIndex();

}
