package dev.rollczi.litecommands;

import java.util.List;

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

    public LiteCommandsException(String message, List<? extends Throwable> exceptions) {
        super(message + " (" + exceptions.size() + " exceptions)");
        exceptions.forEach(exception -> addSuppressed(exception));
    }

    public RuntimeException toRuntimeException() {
        if (getCause() instanceof Exception) {
            return (RuntimeException) getCause();
        }
        return this;
    }

}
