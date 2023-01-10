package dev.rollczi.litecommands.modern.command;

import dev.rollczi.litecommands.modern.command.argument.invocation.FailedReason;
import dev.rollczi.litecommands.shared.Validation;
import org.jetbrains.annotations.Nullable;
import panda.std.Option;

public class CommandExecuteResult {

    private final @Nullable Object result;
    private final @Nullable Exception exception;

    private CommandExecuteResult(@Nullable Object result, @Nullable Exception exception) {
        this.result = result;
        this.exception = exception;
    }

    public Option<Object> getResult() {
        Validation.isTrue(this.isSuccessful(), "Result isn't successful!");

        return Option.of(result);
    }

    public Option<FailedReason> getFailedReason() {
        Validation.isTrue(this.isFailed(), "Result isn't failed!");

        return Option.of(failedReason);
    }

    public boolean isFailed() {
        return failedReason != null;
    }

    public boolean isSuccessful() {
        return failedReason == null;
    }

    public static CommandExecuteResult success(@Nullable Object result) {
        return new CommandExecuteResult(result, null);
    }

    public static CommandExecuteResult failed(FailedReason failedReason) {
        Validation.isNotNull(failedReason, "failed reason can't not be null");

        return new CommandExecuteResult(null, failedReason);
    }

}
