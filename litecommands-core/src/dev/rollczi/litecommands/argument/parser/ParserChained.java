package dev.rollczi.litecommands.argument.parser;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.range.Rangeable;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public interface ParserChained<SENDER, PARSED> extends Rangeable<Argument<PARSED>> {

    ParseResult<PARSED> parse(Invocation<SENDER> invocation, Argument<PARSED> argument, RawInput input, ParserChainAccessor<SENDER> chainAccessor);

    default boolean canParse(Invocation<SENDER> invocation, Argument<PARSED> argument) {
        return true;
    }

}
