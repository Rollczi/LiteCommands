package dev.rollczi.litecommands.annotations.argument;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.ArgumentResult;
import dev.rollczi.litecommands.argument.input.RawInput;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.range.Range;

public abstract class OneAnnotationArgument<SENDER, TYPE> implements MultiAnnotationArgument<SENDER, TYPE> {

    @Override
    public final ArgumentResult<TYPE> parse(Invocation<SENDER> invocation, Argument<TYPE> argument, RawInput rawInput) {
        return this.parse(invocation, rawInput.consumeNext(), (ParameterArgument<Arg, TYPE>) argument);
    }

    protected abstract ArgumentResult<TYPE> parse(Invocation<SENDER> invocation, String argument, ParameterArgument<Arg, TYPE> context);

    @Override
    public final Range getRange() {
        return Range.ONE;
    }

}
