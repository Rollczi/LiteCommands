package dev.rollczi.litecommands.programmatic;

import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.requirement.Requirement;
import dev.rollczi.litecommands.scheduler.SchedulerPoll;

public final class LiteProgrammatic {

    private LiteProgrammatic() {
    }

    public static <R extends Requirement<T>, T> R async(R requirement) {
        requirement.meta().put(Meta.POLL_TYPE, SchedulerPoll.ASYNCHRONOUS);
        return requirement;
    }

}
