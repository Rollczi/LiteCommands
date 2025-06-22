package dev.rollczi.litecommands.minestom;

import dev.rollczi.litecommands.scheduler.SchedulerPoll;

@Deprecated
public class MinestomSchedulerPoll extends SchedulerPoll {

    protected MinestomSchedulerPoll(String name, SchedulerPoll... replaceable) {
        super(name, replaceable);
    }

    protected MinestomSchedulerPoll(String name, boolean logging, SchedulerPoll... replaceable) {
        super(name, logging, replaceable);
    }

    @Deprecated
    public static final MinestomSchedulerPoll TICK_START = new MinestomSchedulerPoll("main_tick_start", SchedulerPoll.MAIN);

    @Deprecated
    public static final MinestomSchedulerPoll TICK_END = new MinestomSchedulerPoll("main_tick_end", SchedulerPoll.MAIN);

}
