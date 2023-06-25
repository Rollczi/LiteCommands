package dev.rollczi.litecommands.annotations.command.requirement;

import java.lang.reflect.Parameter;

public interface NotAnnotatedParameterRequirementFactory<SENDER> {

    ParameterRequirement<SENDER, ?> create(Parameter parameter);

}
