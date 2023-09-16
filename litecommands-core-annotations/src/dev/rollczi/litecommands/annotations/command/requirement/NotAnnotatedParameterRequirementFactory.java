package dev.rollczi.litecommands.annotations.command.requirement;

import dev.rollczi.litecommands.command.requirement.Requirement;

import java.lang.reflect.Parameter;

public interface NotAnnotatedParameterRequirementFactory<SENDER> {

    Requirement<SENDER, ?> create(Parameter parameter);

}
