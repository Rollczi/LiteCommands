package dev.rollczi.litecommands.scheduler;

@Deprecated
public class SchedulerPoll extends SchedulerType {

    @Deprecated
    protected SchedulerPoll(String name, SchedulerType... replaceable) {
        super(name, replaceable);
    }

    @Deprecated
    protected SchedulerPoll(String name, boolean logging, SchedulerType... replaceable) {
        super(name, logging, replaceable);
    }

}
