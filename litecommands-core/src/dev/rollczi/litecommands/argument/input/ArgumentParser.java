package dev.rollczi.litecommands.argument.input;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.ArgumentResult;
import dev.rollczi.litecommands.invocation.Invocation;

public interface ArgumentParser<SENDER, INPUT, PARSED> {

    ArgumentResult<PARSED> parse(Invocation<SENDER> invocation, Argument<PARSED> argument, INPUT input);

    default boolean canParse(Invocation<SENDER> invocation, Argument<PARSED> argument, INPUT rawInput) {
        return true;
    }

    Class<INPUT> getInputType();

}
