package dev.rollczi.litecommands.shared;

import dev.rollczi.litecommands.priority.Prioritized;
import dev.rollczi.litecommands.priority.PriorityLevel;
import org.jetbrains.annotations.ApiStatus;

public class FailedReason implements Prioritized {

    private final Object reason;
    private final PriorityLevel priorityLevel;

    private FailedReason(Object reason, PriorityLevel priorityLevel) {
        Preconditions.notNull(reason, "reason");
        Preconditions.notNull(priorityLevel, "priority");
        Preconditions.isNotInstanceOf(reason, FailedReason.class, "reason");
        this.reason = reason;
        this.priorityLevel = priorityLevel;
    }

    public Object getReason() {
        return this.reason;
    }

    @Override
    public PriorityLevel getPriority() {
        return this.priorityLevel;
    }

    @Override
    public String toString() {
        return "FailedReason(" + reason + ")";
    }

    public static FailedReason of(Object reason) {
        return new FailedReason(reason, PriorityLevel.NORMAL);
    }

    @ApiStatus.Experimental
    public static FailedReason of(Object reason, PriorityLevel priorityLevel) {
        return new FailedReason(reason, priorityLevel);
    }

    @ApiStatus.Experimental
    public static FailedReason max(FailedReason... reasons) {
        FailedReason max = null;
        for (FailedReason reason : reasons) {
            if (reason == null) {
                continue;
            }

            if (max == null) {
                max = reason;
                continue;
            }

            if (reason.getPriority().compareTo(max.getPriority()) > 0) {
                max = reason;
            }
        }

        return max;
    }

}
