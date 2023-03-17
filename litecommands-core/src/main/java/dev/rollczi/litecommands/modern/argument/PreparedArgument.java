package dev.rollczi.litecommands.modern.argument;

import dev.rollczi.litecommands.modern.invocation.Invocation;
import dev.rollczi.litecommands.modern.range.Rangeable;
import dev.rollczi.litecommands.modern.wrapper.Wrappable;

import java.util.List;

public interface PreparedArgument<SENDER, EXPECTED> extends Wrappable<EXPECTED>, Rangeable {

    ArgumentResult<EXPECTED> resolve(Invocation<SENDER> invocation, List<String> arguments);

}
