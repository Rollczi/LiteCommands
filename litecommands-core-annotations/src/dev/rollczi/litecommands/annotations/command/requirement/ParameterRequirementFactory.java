package dev.rollczi.litecommands.annotations.command.requirement;

import dev.rollczi.litecommands.command.requirement.Requirement;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

@Deprecated
public interface ParameterRequirementFactory<SENDER, A extends Annotation> {

    Requirement<SENDER, ?> create(Parameter parameter, A annotation);

}
