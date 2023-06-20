package dev.rollczi.litecommands.command.requirement;

import dev.rollczi.litecommands.argument.parser.input.ParsableInputMatcher;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.scheduler.SchedulerPollType;

public interface Requirement<SENDER, PARSED> {

    <MATCHER extends ParsableInputMatcher<MATCHER>> RequirementResult<PARSED> match(
        Invocation<SENDER> invocation,
        MATCHER matcher
    );

    default SchedulerPollType pollType() {
        return SchedulerPollType.SYNC;
    }

}
