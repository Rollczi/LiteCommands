package dev.rollczi.litecommands.argument;

import dev.rollczi.litecommands.command.requirements.CommandRequirementResult;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.range.Rangeable;

import java.util.List;

public interface PreparedArgument<SENDER, EXPECTED> extends Rangeable {

    CommandRequirementResult<EXPECTED> resolve(Invocation<SENDER> invocation, List<String> arguments);

}
