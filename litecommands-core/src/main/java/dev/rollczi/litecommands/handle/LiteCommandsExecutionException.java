package dev.rollczi.litecommands.handle;

public class LiteCommandsExecutionException extends RuntimeException {

    public LiteCommandsExecutionException() {
    }

    public LiteCommandsExecutionException(String message) {
        super(message);
    }

    public LiteCommandsExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public LiteCommandsExecutionException(Throwable cause) {
        super(cause);
    }
}
