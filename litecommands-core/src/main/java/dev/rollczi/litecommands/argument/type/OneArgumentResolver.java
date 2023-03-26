package dev.rollczi.litecommands.argument.type;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.ArgumentResult;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.range.Range;

import java.util.List;

public abstract class OneArgumentResolver<SENDER, TYPE> implements ArgumentResolver<SENDER, TYPE> {

    @Override
    public final ArgumentResult<TYPE> parse(Invocation<SENDER> invocation, Argument<TYPE> argument, List<String> arguments) {
        if (arguments.isEmpty()) {
            throw new IllegalArgumentException("To parse argument, you need to provide at least one argument.");
        }

        return this.parse(invocation, argument, arguments.get(0));
    }

    @Override
    public final boolean canParse(Invocation<SENDER> invocation, Argument<TYPE> argument, List<String> arguments) {
        if (arguments.isEmpty()) {
            throw new IllegalArgumentException("To parse argument, you need to provide at least one argument.");
        }

        return this.canParse(invocation, argument, arguments.get(0));
    }

    @Override
    public final Range getRange() {
        return Range.ONE;
    }

    protected abstract ArgumentResult<TYPE> parse(Invocation<SENDER> invocation, Argument<TYPE> context, String argument);

    protected boolean canParse(Invocation<SENDER> invocation, Argument<TYPE> context, String argument) {
        return true;
    }

}
