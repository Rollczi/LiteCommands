package dev.rollczi.litecommands.annotations.command;

import dev.rollczi.litecommands.command.requirement.Requirement;

import java.lang.reflect.Parameter;

public interface ParameterRequirement<SENDER, RESULT> extends Requirement<SENDER, RESULT> {

    Parameter getParameter();

    int getParameterIndex();

}
