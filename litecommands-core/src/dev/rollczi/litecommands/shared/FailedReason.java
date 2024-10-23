package dev.rollczi.litecommands.shared;

import dev.rollczi.litecommands.priority.Prioritized;
import dev.rollczi.litecommands.priority.PriorityLevel;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

public class FailedReason implements Prioritized {

    private final Object reason;
    private final boolean isEmpty;
    private final PriorityLevel priorityLevel;

    private FailedReason(Object reason, boolean isEmpty, PriorityLevel priorityLevel) {
        this.reason = reason;
        this.isEmpty = isEmpty;
        this.priorityLevel = priorityLevel;
    }

    public Object getReason() {
        if (this.isEmpty) {
            throw new IllegalStateException("Cannot get reason when it is empty");
        }

        return this.reason;
    }

    public @Nullable Object getReasonOr(Object defaultValue) {
        return this.isEmpty ? defaultValue : this.reason;
    }

    public boolean hasResult() {
        return !this.isEmpty;
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
        return new FailedReason(reason, false, PriorityLevel.NORMAL);
    }

    @ApiStatus.Experimental
    public static FailedReason of(Object reason, PriorityLevel priorityLevel) {
        return new FailedReason(reason, false, priorityLevel);
    }

    @Deprecated
    public static FailedReason empty() {
        return new FailedReason(null, true, PriorityLevel.LOW);
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
