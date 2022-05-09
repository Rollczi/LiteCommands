package dev.rollczi.litecommands.handle;

public class LiteException extends RuntimeException {

    private final Object value;

    public LiteException(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

}
