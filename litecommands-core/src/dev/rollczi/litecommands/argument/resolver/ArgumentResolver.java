package dev.rollczi.litecommands.argument.resolver;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.range.Range;

public abstract class ArgumentResolver<SENDER, TYPE> implements MultipleArgumentResolver<SENDER, TYPE> {

    @Override
    public final ParseResult<TYPE> parse(Invocation<SENDER> invocation, Argument<TYPE> argument, RawInput rawInput) {
        if (!rawInput.hasNext()) {
            throw new IllegalArgumentException("To parse argument, you need to provide at least one argument.");
        }

        return this.parse(invocation, argument, rawInput.next());
    }

    protected abstract ParseResult<TYPE> parse(Invocation<SENDER> invocation, Argument<TYPE> context, String argument);

    @Override
    public final Range getRange(Argument<TYPE> argument) {
        return Range.ONE;
    }

}
