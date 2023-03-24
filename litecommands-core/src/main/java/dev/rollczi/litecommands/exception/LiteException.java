package dev.rollczi.litecommands.exception;

public class LiteException extends RuntimeException {

    private final Object reason;

    public LiteException(Object reason) {
        this.reason = reason;
    }

    public Object getReason() {
        return reason;
    }

}
