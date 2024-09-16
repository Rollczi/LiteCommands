package dev.rollczi.litecommands.shared;

public class AttemptFailedException extends RuntimeException {

    public AttemptFailedException(String message) {
        super(message);
    }

    public AttemptFailedException(String message, Throwable cause) {
        super(message, cause);
    }

}
