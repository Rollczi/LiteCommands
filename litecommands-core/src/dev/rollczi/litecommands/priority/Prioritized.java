package dev.rollczi.litecommands.priority;

public interface Prioritized extends Comparable<Prioritized> {

    PriorityLevel getPriority();

    @Override
    default int compareTo(Prioritized prioritized) {
        return this.getPriority().compareTo(prioritized.getPriority());
    }

    default boolean isHigherThan(Prioritized prioritized) {
        return this.compareTo(prioritized) > 0;
    }

    default boolean isLowerThan(Prioritized prioritized) {
        return this.compareTo(prioritized) < 0;
    }

}
