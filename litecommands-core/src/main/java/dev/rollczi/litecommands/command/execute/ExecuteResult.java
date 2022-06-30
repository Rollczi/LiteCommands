package dev.rollczi.litecommands.command.execute;

import dev.rollczi.litecommands.command.FindResult;

@Deprecated
public class ExecuteResult {

    private final FindResult based;
    private final boolean success;
    private final boolean invalid;
    private final Object result;

    public ExecuteResult(FindResult based, boolean success, boolean invalid, Object result) {
        this.based = based;
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
        return !success && !invalid;
    }

    public Object getResult() {
        return result;
    }

    public FindResult getBased() {
        return based;
    }

    public static ExecuteResult success(FindResult based, Object object) {
        return new ExecuteResult(based, true, false, object);
    }

    public static ExecuteResult invalid(FindResult based, Object object) {
        return new ExecuteResult(based, false, true, object);
    }

    public static ExecuteResult failure(FindResult based) {
        return new ExecuteResult(based, false, false, null);
    }

}
