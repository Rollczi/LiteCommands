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

}
