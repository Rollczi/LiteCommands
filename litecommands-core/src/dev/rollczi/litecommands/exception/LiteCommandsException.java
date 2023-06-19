package dev.rollczi.litecommands.exception;

public class LiteCommandsException extends RuntimeException {

    public LiteCommandsException() {
    }

    public LiteCommandsException(String message) {
        super(message);
    }

    public LiteCommandsException(String message, Throwable cause) {
        super(message, cause);
    }

    public LiteCommandsException(Throwable cause) {
        super(cause);
    }

}
