package dev.rollczi.litecommands.annotations.command;

import dev.rollczi.litecommands.annotations.async.Async;
import dev.rollczi.litecommands.command.requirement.Requirement;
import dev.rollczi.litecommands.scheduler.SchedulerPollType;

import java.lang.reflect.Parameter;

public interface ParameterRequirement<SENDER, RESULT> extends Requirement<SENDER, RESULT> {

    Parameter getParameter();

    int getParameterIndex();

    @Override
    default SchedulerPollType pollType() {
        if (getParameter().isAnnotationPresent(Async.class)) {
            return SchedulerPollType.ASYNC;
        }

        return SchedulerPollType.SYNC;
    }

}
