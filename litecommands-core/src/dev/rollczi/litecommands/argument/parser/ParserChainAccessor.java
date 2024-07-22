package dev.rollczi.litecommands.argument.parser;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.invocation.Invocation;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public interface ParserChainAccessor<SENDER> {

    <T> ParseResult<T> parse(Invocation<SENDER> invocation, Argument<T> argument, RawInput input);

    default <T> ParseResult<T> parse(Invocation<SENDER> invocation, Argument<T> argument, String input) {
        return parse(invocation, argument, RawInput.of(input));
    }

}
