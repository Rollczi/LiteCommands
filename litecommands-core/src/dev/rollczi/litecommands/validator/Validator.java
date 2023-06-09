package dev.rollczi.litecommands.validator;

import dev.rollczi.litecommands.flow.Flow;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.command.CommandExecutor;
import dev.rollczi.litecommands.command.CommandRoute;

public interface Validator<SENDER> {

    Flow validate(Invocation<SENDER> invocation, CommandRoute<SENDER> command, CommandExecutor<SENDER> executor);

}
