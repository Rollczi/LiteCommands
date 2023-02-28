package dev.rollczi.litecommands.modern.argument;

import dev.rollczi.litecommands.modern.range.Rangeable;
import dev.rollczi.litecommands.modern.invocation.Invocation;

import java.util.List;

public interface ArgumentParser<SENDER, EXPECTED, ARGUMENT extends Argument<EXPECTED>> extends Rangeable {

    ArgumentResult<EXPECTED> parse(Invocation<SENDER> invocation, ARGUMENT argument, List<String> arguments);

    default boolean canParse(Invocation<SENDER> invocation, ARGUMENT argument, List<String> arguments) {
        return true;
    }

}
