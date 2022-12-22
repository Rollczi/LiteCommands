package dev.rollczi.litecommands.modern.command.argument.invocation;

import dev.rollczi.litecommands.command.Invocation;
import dev.rollczi.litecommands.command.count.CountRange;

import java.util.List;

public interface ArgumentInvocationParser<SENDER, EXPECTED> {

    ArgumentResult<EXPECTED> parse(Invocation<SENDER> invocation, List<String> arguments);

    CountRange getArgumentsRange();

}
