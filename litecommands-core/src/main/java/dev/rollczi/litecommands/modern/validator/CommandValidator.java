package dev.rollczi.litecommands.modern.validator;

import dev.rollczi.litecommands.modern.command.CommandExecutor;
import dev.rollczi.litecommands.modern.command.CommandRoute;
import dev.rollczi.litecommands.modern.invocation.Invocation;

public interface CommandValidator<SENDER> {

    CommandValidatorResult validate(Invocation<SENDER> invocation, CommandRoute<SENDER> command, CommandExecutor<SENDER> executor);

}
