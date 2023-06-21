package dev.rollczi.litecommands.scheduler;

public class ScheduledChainException extends RuntimeException {

    private final Object reason;

    public ScheduledChainException(Object reason) {
        this.reason = reason;
    }

    public Object getReason() {
        return reason;
    }

}
