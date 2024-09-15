package dev.rollczi.litecommands.priority;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Experimental
public final class PriorityLevel implements Comparable<PriorityLevel> {

    public static final PriorityLevel NONE = new PriorityLevel("NONE", Integer.MIN_VALUE);
    public static final PriorityLevel LOWEST = new PriorityLevel("LOWEST", -1000);
    public static final PriorityLevel VERY_LOW = new PriorityLevel("LOW", -500);
    public static final PriorityLevel LOW = new PriorityLevel("LOW", -100);
    public static final PriorityLevel BELOW_NORMAL = new PriorityLevel("BELOW_NORMAL", -50);
    public static final PriorityLevel NORMAL = new PriorityLevel("NORMAL", 0);
    public static final PriorityLevel ABOVE_NORMAL = new PriorityLevel("ABOVE_NORMAL", 50);
    public static final PriorityLevel HIGH = new PriorityLevel("HIGH", 100);
    public static final PriorityLevel VERY_HIGH = new PriorityLevel("HIGH", 500);
    public static final PriorityLevel HIGHEST = new PriorityLevel("HIGHEST", 1000);
    public static final PriorityLevel MAX = new PriorityLevel("MAX", Integer.MAX_VALUE);

    private final String name;
    private final int priority;

    @ApiStatus.Experimental
    public PriorityLevel(String name, int priority) {
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
    public int compareTo(@NotNull PriorityLevel o) {
        return Integer.compare(this.priority, o.priority);
    }

}
