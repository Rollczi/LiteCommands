package dev.rollczi.litecommands.argument.parser;

import dev.rollczi.litecommands.LiteCommandsException;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.matcher.Matcher;
import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.range.Rangeable;
import dev.rollczi.litecommands.requirement.RequirementResult;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public interface ParserChained<SENDER, T> extends Matcher<SENDER, T>, Rangeable<Argument<T>> {

    ParseResult<T> parse(Invocation<SENDER> invocation, Argument<T> argument, RawInput input, ParserChainAccessor<SENDER> chainAccessor);

    default boolean canParse(Argument<T> argument) {
        return true;
    }

    default boolean match(Invocation<SENDER> invocation, Argument<T> argument, RawInput input, ParserChainAccessor<SENDER> accessor) {
        ParseResult<T> parsed = this.parse(invocation, argument, input, accessor);

        if (parsed instanceof RequirementResult) {
            RequirementResult<T> completed = (RequirementResult<T>) parsed;

            return completed.isSuccessful() || completed.isSuccessfulNull();
        }

        throw new LiteCommandsException("Async parsers should override Parser#match method! (" + this.getClass().getName() + ")");
    }

}
