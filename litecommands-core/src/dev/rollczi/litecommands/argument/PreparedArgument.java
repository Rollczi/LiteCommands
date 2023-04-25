package dev.rollczi.litecommands.argument;

import dev.rollczi.litecommands.command.requirements.CommandRequirementResult;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.range.Rangeable;
import dev.rollczi.litecommands.wrapper.Wrappable;

import java.util.List;

public interface PreparedArgument<SENDER, EXPECTED> extends Wrappable<EXPECTED>, Rangeable {

    CommandRequirementResult<EXPECTED> resolve(Invocation<SENDER> invocation, List<String> arguments);

}
