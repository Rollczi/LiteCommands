package dev.rollczi.litecommands.argument.parser;

import dev.rollczi.litecommands.LiteCommandsException;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.range.Rangeable;
import dev.rollczi.litecommands.requirement.RequirementResult;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public interface ParserChained<SENDER, PARSED> extends Rangeable<Argument<PARSED>> {

    ParseResult<PARSED> parse(Invocation<SENDER> invocation, Argument<PARSED> argument, RawInput input, ParserChainAccessor<SENDER> chainAccessor);

    default boolean canParse(Argument<PARSED> argument) {
        return true;
    }

    default boolean matchParse(Invocation<SENDER> invocation, Argument<PARSED> argument, RawInput input, ParserChainAccessor<SENDER> accessor) {
        ParseResult<PARSED> parsed = this.parse(invocation, argument, input, accessor);

        if (parsed instanceof RequirementResult) {
            RequirementResult<PARSED> completed = (RequirementResult<PARSED>) parsed;

            return completed.isSuccessful() || completed.isSuccessfulNull();
        }

        throw new LiteCommandsException("Async parsers should override Parser#matchParse method! (" + this.getClass().getName() + ")");
    }

}
