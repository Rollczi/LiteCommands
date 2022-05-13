package dev.rollczi.litecommands.handle;

public class LiteException extends RuntimeException {

    private final Object result;

    public LiteException(Object result) {
        this.result = result;
    }

    public Object getResult() {
        return result;
    }

}
