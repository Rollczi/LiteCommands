package dev.rollczi.litecommands.modern.command;

import dev.rollczi.litecommands.modern.argument.FailedReason;
import dev.rollczi.litecommands.modern.util.Preconditions;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class CommandExecutorMatchResult {

    private final @Nullable Supplier<CommandExecuteResult> preparedExecutor;
    private final @Nullable FailedReason failedReason;

    private CommandExecutorMatchResult(@Nullable Supplier<CommandExecuteResult> preparedExecutor, @Nullable FailedReason failedReason) {
        this.preparedExecutor = preparedExecutor;
        this.failedReason = failedReason;
    }

    public CommandExecuteResult executeCommand() {
        Preconditions.checkState(preparedExecutor != null, "Cannot execute command when it failed");

        return preparedExecutor.get();
    }

    public FailedReason getFailedReason() {
        Preconditions.checkState(isFailed(), "Cannot get failed reason when command was successful");

        return failedReason;
    }

    public boolean isFailed() {
        return failedReason != null;
    }

    public boolean isSuccessful() {
        return preparedExecutor != null;
    }

    public static CommandExecutorMatchResult success(Supplier<CommandExecuteResult> preparedExecutor) {
        return new CommandExecutorMatchResult(preparedExecutor, null);
    }

    public static CommandExecutorMatchResult failed(FailedReason failedReason) {
        return new CommandExecutorMatchResult(null, failedReason);
    }

    public static CommandExecutorMatchResult failed(Object failedReason) {
        return new CommandExecutorMatchResult(null, FailedReason.of(failedReason));
    }

}
