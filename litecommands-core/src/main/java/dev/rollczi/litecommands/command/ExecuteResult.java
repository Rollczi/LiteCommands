package dev.rollczi.litecommands.command;

public class ExecuteResult {

    private final boolean success;
    private final boolean invalid;
    private final Object result;

    public ExecuteResult(boolean success, boolean invalid, Object result) {
        this.success = success;
        this.invalid = invalid;
        this.result = result;
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean isInvalid() {
        return invalid;
    }

    public boolean isFailure() {
        return !success;
    }

    public Object getResult() {
        return result;
    }

    public static ExecuteResult success(Object object) {
        return new ExecuteResult(true, false, object);
    }

    public static ExecuteResult invalid(Object object) {
        return new ExecuteResult(false, true, object);
    }

    public static ExecuteResult failure() {
        return new ExecuteResult(false, false, null);
    }

}
