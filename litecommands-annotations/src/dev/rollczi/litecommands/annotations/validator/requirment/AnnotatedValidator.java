package dev.rollczi.litecommands.annotations.validator.requirment;

import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.requirement.Requirement;
import dev.rollczi.litecommands.validator.ValidatorResult;

import java.lang.annotation.Annotation;

@FunctionalInterface
public interface AnnotatedValidator<SENDER, T, A extends Annotation> {

    ValidatorResult validate(Invocation<SENDER> invocation, CommandExecutor<SENDER> executor, Requirement<T> requirement, T value, A annotation);

}
