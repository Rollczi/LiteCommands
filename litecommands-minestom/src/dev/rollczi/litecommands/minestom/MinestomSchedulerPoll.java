package dev.rollczi.litecommands.minestom;

import dev.rollczi.litecommands.scheduler.SchedulerPoll;

public class MinestomSchedulerPoll extends SchedulerPoll {

    protected MinestomSchedulerPoll(String name, SchedulerPoll... replaceable) {
        super(name, replaceable);
    }

    protected MinestomSchedulerPoll(String name, boolean logging, SchedulerPoll... replaceable) {
        super(name, logging, replaceable);
    }

    public static final MinestomSchedulerPoll TICK_START = new MinestomSchedulerPoll("main_tick_start", SchedulerPoll.MAIN);
    public static final MinestomSchedulerPoll TICK_END = new MinestomSchedulerPoll("main_tick_end", SchedulerPoll.MAIN);

}
