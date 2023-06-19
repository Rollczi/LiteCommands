package dev.rollczi.litecommands.invalid;

import dev.rollczi.litecommands.exception.LiteCommandsException;

public class InvalidUsageException extends LiteCommandsException {

    private final Object result;

    public InvalidUsageException(Object result) {
        this.result = result;
    }

    public Object getResult() {
        return result;
    }

}
