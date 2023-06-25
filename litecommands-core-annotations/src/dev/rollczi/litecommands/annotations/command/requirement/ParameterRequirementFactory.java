package dev.rollczi.litecommands.annotations.command.requirement;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

public interface ParameterRequirementFactory<SENDER, A extends Annotation> {

    ParameterRequirement<SENDER, ?> create(Parameter parameter, A annotation);

}
