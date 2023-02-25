package dev.rollczi.litecommands.modern.argument.api;

import dev.rollczi.litecommands.command.count.CountRange;
import dev.rollczi.litecommands.modern.argument.Argument;
import dev.rollczi.litecommands.modern.argument.ArgumentResult;
import dev.rollczi.litecommands.modern.invocation.Invocation;

import java.util.List;

public abstract class OneArgument<SENDER, TYPE> implements MultiArgument<SENDER, TYPE> {

    @Override
    public final ArgumentResult<TYPE> parse(Invocation<SENDER> invocation, List<String> arguments, Argument<Object, TYPE> context) {
        return this.parse(invocation, context, arguments.get(0));
    }

    protected abstract ArgumentResult<TYPE> parse(Invocation<SENDER> invocation, Argument<Object, TYPE> context, String argument);

    @Override
    public final CountRange getRange() {
        return CountRange.ONE;
    }
}
