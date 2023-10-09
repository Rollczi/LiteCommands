package dev.rollczi.litecommands.argument.parser;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.range.Rangeable;

public interface Parser<SENDER, INPUT, PARSED> extends Rangeable<Argument<PARSED>> {

    ParseResult<PARSED> parse(Invocation<SENDER> invocation, Argument<PARSED> argument, INPUT input);

    default boolean canParse(Invocation<SENDER> invocation, Argument<PARSED> argument, INPUT rawInput) {
        return true;
    }

    Class<INPUT> getInputType();

}
