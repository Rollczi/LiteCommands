package dev.rollczi.litecommands.argument;

import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.range.Rangeable;
import dev.rollczi.litecommands.wrapper.Wrappable;

import java.util.List;

public interface PreparedArgument<SENDER, EXPECTED> extends Wrappable<EXPECTED>, Rangeable {

    PreparedArgumentResult<EXPECTED> resolve(Invocation<SENDER> invocation, List<String> arguments);

}
