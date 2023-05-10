package dev.rollczi.litecommands.handle;

import dev.rollczi.litecommands.command.InvalidUsage;

public class LiteException extends RuntimeException {

    private final Object result;

    public LiteException(Object result) {
        this.result = result;
    }

    public Object getResult() {
        return result;
    }

    public static LiteException newInvalidUsage() {
        return new LiteException(InvalidUsage.INSTANCE);
    }

}
