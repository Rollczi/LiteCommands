package dev.rollczi.litecommands.invalidusage;

import dev.rollczi.litecommands.shared.Preconditions;

public class InvalidUsageException extends RuntimeException {

    private final Object errorResult;

    public InvalidUsageException(Object errorResult) {
        Preconditions.notNull(errorResult, "errorResult");
        this.errorResult = errorResult;
    }

    public Object getErrorResult() {
        return errorResult;
    }

}
