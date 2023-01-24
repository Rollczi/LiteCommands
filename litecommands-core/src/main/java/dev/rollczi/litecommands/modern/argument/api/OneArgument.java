package dev.rollczi.litecommands.modern.argument.api;

import dev.rollczi.litecommands.command.count.CountRange;
import dev.rollczi.litecommands.modern.argument.ArgumentContextual;
import dev.rollczi.litecommands.modern.argument.ArgumentResult;
import dev.rollczi.litecommands.modern.invocation.Invocation;

import java.util.List;

public abstract class OneArgument<SENDER, TYPE> implements MultiArgument<SENDER, TYPE> {

    @Override
    public final ArgumentResult<TYPE> parse(Invocation<SENDER> invocation, List<String> arguments, ArgumentContextual<Object, TYPE> context) {
        return this.parse(invocation, context, arguments.get(0));
    }

    protected abstract ArgumentResult<TYPE> parse(Invocation<SENDER> invocation, ArgumentContextual<Object, TYPE> context, String argument);

    @Override
    public final CountRange getRange() {
        return CountRange.ONE;
    }
}
