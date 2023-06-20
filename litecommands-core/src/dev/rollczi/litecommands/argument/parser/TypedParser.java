package dev.rollczi.litecommands.argument.parser;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.invocation.Invocation;

public interface TypedParser<SENDER, INPUT, PARSED, ARGUMENT extends Argument<PARSED>> extends Parser<SENDER, INPUT, PARSED> {

    ParseResult<PARSED> parseTyped(Invocation<SENDER> invocation, ARGUMENT argument, INPUT input);

    Class<? extends Argument> getArgumentType();

    @Override
    @SuppressWarnings("unchecked")
    default ParseResult<PARSED> parse(Invocation<SENDER> invocation, Argument<PARSED> argument, INPUT input) {
        return parseTyped(invocation, (ARGUMENT) argument, input);
    }

}
