package dev.rollczi.litecommands.command.requirement;

import dev.rollczi.litecommands.argument.input.ArgumentsInputMatcher;
import dev.rollczi.litecommands.invocation.Invocation;

public interface Requirement<SENDER, PARSED> {

    <MATCHER extends ArgumentsInputMatcher<MATCHER>> RequirementResult<PARSED> match(
        Invocation<SENDER> invocation,
        MATCHER matcher
    );

}
