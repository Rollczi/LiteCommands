package dev.rollczi.litecommands.shared;

import org.jetbrains.annotations.Nullable;

public class FailedReason {

    private final Object reason;
    private final boolean isEmpty;

    private FailedReason(Object reason, boolean isEmpty) {
        this.reason = reason;
        this.isEmpty = isEmpty;
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

    public static FailedReason of(Object reason) {
        return new FailedReason(reason, false);
    }

    @Deprecated
    public static FailedReason empty() {
        return new FailedReason(null, true);
    }

    @Override
    public String toString() {
        return "FailedReason(" + reason + ")";
    }

}
