package dev.rollczi.litecommands.argument.resolver;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.parser.ParserChainAccessor;
import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.range.Range;
import dev.rollczi.litecommands.requirement.RequirementResult;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public abstract class ArgumentResolverChained<SENDER, T> implements MultipleArgumentResolverChained<SENDER, T> {

    @Override
    public final ParseResult<T> parse(Invocation<SENDER> invocation, Argument<T> argument, RawInput input, ParserChainAccessor<SENDER> chainAccessor) {
        if (!input.hasNext()) {
            return ParseResult.failure(InvalidUsage.Cause.MISSING_ARGUMENT);
        }

        return this.parse(invocation, argument, input.next(), chainAccessor);
    }

    protected abstract ParseResult<T> parse(Invocation<SENDER> invocation, Argument<T> argument, String input, ParserChainAccessor<SENDER> chainAccessor);

    @Override
    public final boolean matchParse(Invocation<SENDER> invocation, Argument<T> argument, RawInput input, ParserChainAccessor<SENDER> accessor) {
        if (!input.hasNext()) {
            return false;
        }

        return this.matchParse(invocation, argument, input.next(), accessor);
    }

    protected boolean matchParse(Invocation<SENDER> invocation, Argument<T> context, String argument, ParserChainAccessor<SENDER> accessor) {
        ParseResult<T> parsed = this.parse(invocation, context, argument, accessor);

        if (parsed instanceof RequirementResult) {
            RequirementResult<T> completed = (RequirementResult<T>) parsed;

            return completed.isSuccessful() || completed.isSuccessfulNull();
        }

        throw new IllegalArgumentException("Async parsers should override ArgumentResolver#matchParse method! (" + this.getClass().getName() + ")");
    }

    @Override
    public final Range getRange(Argument<T> argument) {
        return Range.ONE;
    }

}
