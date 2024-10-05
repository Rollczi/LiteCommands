package dev.rollczi.litecommands.argument.matcher;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.invocation.Invocation;
import org.jetbrains.annotations.ApiStatus;

/**
 * Matcher is used to match a command input without parsing.
 */
@ApiStatus.Experimental
public interface Matcher<SENDER, T> {

    @ApiStatus.Experimental
    default boolean match(Invocation<SENDER> invocation, Argument<T> argument, RawInput input) {
        return true;
    }

}
