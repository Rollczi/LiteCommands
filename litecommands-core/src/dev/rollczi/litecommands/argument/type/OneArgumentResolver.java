package dev.rollczi.litecommands.argument.type;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.ArgumentResult;
import dev.rollczi.litecommands.argument.input.RawInput;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.range.Range;

public abstract class OneArgumentResolver<SENDER, TYPE> implements MultipleArgumentResolver<SENDER, TYPE> {

    @Override
    public final ArgumentResult<TYPE> parse(Invocation<SENDER> invocation, Argument<TYPE> argument, RawInput rawInput) {
        if (rawInput.hasNoNext()) {
            throw new IllegalArgumentException("To parse argument, you need to provide at least one argument.");
        }

        return this.parse(invocation, argument, rawInput.consumeNext());
    }

    @Override
    public final boolean canParse(Invocation<SENDER> invocation, Argument<TYPE> argument, RawInput rawInput) {
        if (rawInput.hasNoNext()) {
            throw new IllegalArgumentException("To parse argument, you need to provide at least one argument.");
        }

        return this.canParse(invocation, argument, rawInput.consumeNext());
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
