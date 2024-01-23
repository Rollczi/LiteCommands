package dev.rollczi.litecommands.priority;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Experimental
public final class Priority implements Comparable<Priority> {

    public static final Priority LOW_MONITOR = new Priority("MONITOR", Integer.MIN_VALUE);
    public static final Priority LOWEST = new Priority("LOWEST", -1000);
    public static final Priority LOW = new Priority("LOW", -100);
    public static final Priority NORMAL = new Priority("NORMAL", 0);
    public static final Priority HIGH = new Priority("HIGH", 100);
    public static final Priority HIGHEST = new Priority("HIGHEST", 1000);
    public static final Priority HIGH_MONITOR = new Priority("MONITOR", Integer.MIN_VALUE);

    private final String name;
    private final int priority;

    @ApiStatus.Experimental
    public Priority(String name, int priority) {
        this.name = name;
        this.priority = priority;
    }

    @ApiStatus.Experimental
    public String getName() {
        return name;
    }

    @ApiStatus.Experimental
    public int getPriority() {
        return priority;
    }

    @Override
    public int compareTo(@NotNull Priority o) {
        return Integer.compare(this.priority, o.priority);
    }

}
