package dev.rollczi.litecommands.command.requirements;

import dev.rollczi.litecommands.argument.input.InputArgumentsMatcher;
import dev.rollczi.litecommands.invocation.Invocation;

public interface CommandRequirement<SENDER, PARSED> {

    <MATCHER extends InputArgumentsMatcher<MATCHER>> CommandRequirementResult<PARSED> check(
        Invocation<SENDER> invocation,
        MATCHER matcher
    );

}
