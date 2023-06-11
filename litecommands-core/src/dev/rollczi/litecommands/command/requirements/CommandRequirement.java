package dev.rollczi.litecommands.command.requirements;

import dev.rollczi.litecommands.argument.input.ArgumentsInputMatcher;
import dev.rollczi.litecommands.invocation.Invocation;

public interface CommandRequirement<SENDER, PARSED> {

    <MATCHER extends ArgumentsInputMatcher<MATCHER>> CommandRequirementResult<PARSED> check(
        Invocation<SENDER> invocation,
        MATCHER matcher
    );

}
