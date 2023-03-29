package dev.rollczi.litecommands.validator;

import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.command.CommandExecutor;
import dev.rollczi.litecommands.command.CommandRoute;

public interface CommandValidator<SENDER> {

    CommandValidatorResult validate(Invocation<SENDER> invocation, CommandRoute<SENDER> command, CommandExecutor<SENDER> executor);

}
