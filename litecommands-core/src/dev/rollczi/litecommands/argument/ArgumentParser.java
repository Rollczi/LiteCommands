package dev.rollczi.litecommands.argument;

import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.range.Rangeable;

import java.util.List;

public interface ArgumentParser<SENDER, EXPECTED, ARGUMENT extends Argument<EXPECTED>> extends Rangeable {

    ArgumentResult<EXPECTED> parse(Invocation<SENDER> invocation, ARGUMENT argument, List<String> arguments);

    default boolean canParse(Invocation<SENDER> invocation, ARGUMENT argument, List<String> arguments) {
        return true;
    }

}
