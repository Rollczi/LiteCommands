package dev.rollczi.litecommands.argument.parser;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.reflect.type.TypeToken;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public interface ParserChainAccessor<SENDER> {

    <T> ParseResult<T> parse(Invocation<SENDER> invocation, Argument<T> argument, RawInput input);

    default <T> ParseResult<T> parse(Invocation<SENDER> invocation, Argument<T> argument, String input) {
        return parse(invocation, argument, RawInput.of(input));
    }

    default <T> ParseResult<T> parse(Invocation<SENDER> invocation, Class<T> type, String input) {
        return parse(invocation, type, RawInput.of(input));
    }

    default <T> ParseResult<T> parse(Invocation<SENDER> invocation, Class<T> type, RawInput input) {
        return parse(invocation, Argument.of("default", TypeToken.of(type)), input);
    }

}
