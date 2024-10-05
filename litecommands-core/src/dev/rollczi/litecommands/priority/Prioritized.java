package dev.rollczi.litecommands.priority;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public interface Prioritized extends Comparable<Prioritized> {

    @ApiStatus.Experimental
    PriorityLevel getPriority();

    @ApiStatus.Experimental
    @Override
    default int compareTo(Prioritized prioritized) {
        return this.getPriority().compareTo(prioritized.getPriority());
    }

    @ApiStatus.Experimental
    default boolean isHigherThan(Prioritized prioritized) {
        return this.compareTo(prioritized) > 0;
    }

    @ApiStatus.Experimental
    default boolean isLowerThan(Prioritized prioritized) {
        return this.compareTo(prioritized) < 0;
    }

}
