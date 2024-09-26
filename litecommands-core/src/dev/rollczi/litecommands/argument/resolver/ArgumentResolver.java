package dev.rollczi.litecommands.argument.resolver;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.range.Range;
import dev.rollczi.litecommands.requirement.RequirementResult;

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

    @Override
    public final boolean matchParse(Invocation<SENDER> invocation, Argument<TYPE> argument, RawInput input) {
        if (!input.hasNext()) {
            return false;
        }

        return this.matchParse(invocation, argument, input.next());
    }

    protected boolean matchParse(Invocation<SENDER> invocation, Argument<TYPE> context, String argument) {
        ParseResult<TYPE> parsed = this.parse(invocation, context, argument);

        if (parsed instanceof RequirementResult) {
            RequirementResult<TYPE> completed = (RequirementResult<TYPE>) parsed;

            return completed.isSuccessful() || completed.isSuccessfulNull();
        }

        throw new IllegalArgumentException("Async parsers should override ArgumentResolver#matchParse method! (" + this.getClass().getName() + ")");
    }

}
