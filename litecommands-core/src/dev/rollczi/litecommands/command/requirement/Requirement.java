package dev.rollczi.litecommands.command.requirement;

import dev.rollczi.litecommands.argument.parser.input.ParseableInputMatcher;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.meta.MetaHolder;

public interface Requirement<SENDER, PARSED> extends MetaHolder {

    String getName();

    <MATCHER extends ParseableInputMatcher<MATCHER>> RequirementResult<PARSED> match(
        Invocation<SENDER> invocation,
        MATCHER matcher
    );

}
