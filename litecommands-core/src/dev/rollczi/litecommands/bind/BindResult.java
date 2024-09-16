package dev.rollczi.litecommands.bind;

import dev.rollczi.litecommands.shared.Preconditions;

public final class BindResult<T> {

    private final T success;
    private final String error;

    private BindResult(T success, String error) {
        this.success = success;
        this.error = error;
    }

    public boolean isOk() {
        return success != null;
    }

    public boolean isError() {
        return error != null;
    }

    public T getSuccess() {
        Preconditions.checkArgument(isOk(), "Cannot get success value from error result");
        return success;
    }

    public String getError() {
        Preconditions.checkArgument(isError(), "Cannot get error value from success result");
        return error;
    }

    public static <T> BindResult<T> ok(T success) {
        Preconditions.notNull(success, "Success cannot be null");
        return new BindResult<>(success, null);
    }

    public static <T> BindResult<T> error(String error) {
        Preconditions.notNull(error, "Error cannot be null");
        return new BindResult<>(null, error);
    }

}
