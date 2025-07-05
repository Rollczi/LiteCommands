package dev.rollczi.litecommands.minestom;

import dev.rollczi.litecommands.scheduler.SchedulerType;

@Deprecated
public class MinestomSchedulerPoll extends SchedulerType {

    protected MinestomSchedulerPoll(String name, SchedulerType... replaceable) {
        super(name, replaceable);
    }

    protected MinestomSchedulerPoll(String name, boolean logging, SchedulerType... replaceable) {
        super(name, logging, replaceable);
    }

    @Deprecated
    public static final MinestomSchedulerPoll TICK_START = new MinestomSchedulerPoll("main_tick_start", SchedulerType.MAIN);

    @Deprecated
    public static final MinestomSchedulerPoll TICK_END = new MinestomSchedulerPoll("main_tick_end", SchedulerType.MAIN);

}
