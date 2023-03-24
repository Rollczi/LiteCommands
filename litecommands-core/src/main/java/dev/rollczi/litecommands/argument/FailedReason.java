package dev.rollczi.litecommands.argument;

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

    public boolean isEmpty() {
        return this.isEmpty;
    }

    public static FailedReason of(Object reason) {
        return new FailedReason(reason, false);
    }

    public static FailedReason empty() {
        return new FailedReason(null, true);
    }

    @Override
    public String toString() {
        return "FailedReason(" + reason + ")";
    }
}
