package dev.rollczi.litecommands.validator.requirement;

import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.requirement.Requirement;
import dev.rollczi.litecommands.validator.ValidatorResult;

public interface RequirementValidator<SENDER, T> {

    ValidatorResult validate(Invocation<SENDER> invocation, CommandExecutor<SENDER> executor, Requirement<T> requirement, T value);

}
