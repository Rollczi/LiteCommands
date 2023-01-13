package dev.rollczi.litecommands.modern.command.api;

import dev.rollczi.litecommands.modern.command.Invocation;
import dev.rollczi.litecommands.command.count.CountRange;
import dev.rollczi.litecommands.modern.command.argument.ArgumentContextual;
import dev.rollczi.litecommands.modern.command.argument.invocation.ArgumentResult;

import java.util.List;

public abstract class OneArgument<SENDER, TYPE> implements MultiArgument<SENDER, TYPE> {

    @Override
    public final ArgumentResult<TYPE> parse(Invocation<SENDER> invocation, List<String> arguments, ArgumentContextual<Object, TYPE> context) {
        return parse(invocation, context, arguments.get(0));
    }

    protected abstract ArgumentResult<TYPE> parse(Invocation<SENDER> invocation, ArgumentContextual<Object, TYPE> context, String argument);

    @Override
    public final CountRange getRange() {
        return CountRange.ONE;
    }
}
