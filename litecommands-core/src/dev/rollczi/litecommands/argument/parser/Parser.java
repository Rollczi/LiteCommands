package dev.rollczi.litecommands.argument.parser;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.range.Rangeable;

public interface Parser<SENDER, PARSED> extends Rangeable<Argument<PARSED>> {

    ParseResult<PARSED> parse(Invocation<SENDER> invocation, Argument<PARSED> argument, RawInput input);

    default boolean canParse(Invocation<SENDER> invocation, Argument<PARSED> argument) {
        return true;
    }

}
