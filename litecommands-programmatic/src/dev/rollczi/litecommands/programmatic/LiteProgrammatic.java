package dev.rollczi.litecommands.programmatic;

import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.requirement.Requirement;
import dev.rollczi.litecommands.scheduler.SchedulerType;

public final class LiteProgrammatic {

    private LiteProgrammatic() {
    }

    public static <R extends Requirement<T>, T> R async(R requirement) {
        requirement.meta().put(Meta.POLL_TYPE, SchedulerType.ASYNCHRONOUS);
        return requirement;
    }

}
