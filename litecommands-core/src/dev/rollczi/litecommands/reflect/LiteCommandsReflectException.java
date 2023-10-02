package dev.rollczi.litecommands.reflect;

import dev.rollczi.litecommands.LiteCommandsException;

import java.util.List;

public class LiteCommandsReflectException extends LiteCommandsException {

    public LiteCommandsReflectException() {
    }

    public LiteCommandsReflectException(String message) {
        super(message);
    }

    public LiteCommandsReflectException(String message, Throwable cause) {
        super(message, cause);
    }

    public LiteCommandsReflectException(Throwable cause) {
        super(cause);
    }

    public LiteCommandsReflectException(String message, List<? extends Throwable> exceptions) {
        super(message, exceptions);
    }

}
