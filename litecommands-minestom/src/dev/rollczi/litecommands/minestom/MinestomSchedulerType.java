package dev.rollczi.litecommands.minestom;

import dev.rollczi.litecommands.scheduler.SchedulerType;

public class MinestomSchedulerType extends SchedulerType {

    protected MinestomSchedulerType(String name, SchedulerType... replaceable) {
        super(name, replaceable);
    }

    protected MinestomSchedulerType(String name, boolean logging, SchedulerType... replaceable) {
        super(name, logging, replaceable);
    }

    public static final MinestomSchedulerType TICK_START = new MinestomSchedulerType("main_tick_start", SchedulerType.MAIN);

    public static final MinestomSchedulerType TICK_END = new MinestomSchedulerType("main_tick_end", SchedulerType.MAIN);

}
