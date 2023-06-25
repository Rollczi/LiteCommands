package dev.rollczi.litecommands.command.executor;

import dev.rollczi.litecommands.shared.FailedReason;
import dev.rollczi.litecommands.shared.Preconditions;
import org.jetbrains.annotations.Nullable;

public class CommandExecuteResult {

    private final @Nullable Object result;
    private final @Nullable Throwable throwable;
    private final @Nullable Object error;

    private CommandExecuteResult(@Nullable Object result, @Nullable Throwable throwable, @Nullable Object error) {
        this.result = result;
        this.throwable = throwable;
        this.error = error;
    }

    public @Nullable Object getResult() {
        return this.result;
    }

    public @Nullable Throwable getThrowable() {
        return this.throwable;
    }

    public @Nullable Object getError() {
        return error;
    }

    public boolean isSuccessful() {
        return this.result != null;
    }

    public boolean isFailed() {
        return this.error != null;
    }

    public boolean isThrown() {
        return this.throwable != null;
    }

    public static CommandExecuteResult success(Object result) {
        return new CommandExecuteResult(result, null, null);
    }

    public static CommandExecuteResult thrown(Throwable exception) {
        Preconditions.notNull(exception, "exception cannot be null");

        return new CommandExecuteResult(null, exception, null);
    }

    public static CommandExecuteResult failed(Object error) {
        Preconditions.notNull(error, "failed cannot be null");

        return new CommandExecuteResult(null, null, error);
    }

    public static CommandExecuteResult failed(FailedReason failedReason) {
        Preconditions.notNull(failedReason, "failed cannot be null");

        return new CommandExecuteResult(null, null, failedReason.getReasonOr(null));
    }

}
