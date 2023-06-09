package dev.rollczi.litecommands.scheduler;

public class ScheduledChainException extends RuntimeException {

    private final ScheduledChainLink<?> link;

    public ScheduledChainException(ScheduledChainLink<?> link) {
        this.link = link;
    }

    public ScheduledChainException(String message, ScheduledChainLink<?> link) {
        super(message);
        this.link = link;
    }

    public ScheduledChainException(String message, Throwable cause, ScheduledChainLink<?> link) {
        super(message, cause);
        this.link = link;
    }

    public ScheduledChainException(Throwable cause, ScheduledChainLink<?> link) {
        super(cause);
        this.link = link;
    }

    public ScheduledChainLink<?> getLink() {
        return link;
    }

}
