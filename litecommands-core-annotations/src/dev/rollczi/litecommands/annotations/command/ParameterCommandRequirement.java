package dev.rollczi.litecommands.annotations.command;

import dev.rollczi.litecommands.command.requirement.CommandRequirement;

import java.lang.reflect.Parameter;

public interface ParameterCommandRequirement<SENDER, RESULT> extends CommandRequirement<SENDER, RESULT> {

    Parameter getParameter();

    int getParameterIndex();

}
