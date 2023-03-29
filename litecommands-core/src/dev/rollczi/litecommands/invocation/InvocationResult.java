package dev.rollczi.litecommands.invocation;

import dev.rollczi.litecommands.argument.FailedReason;
import dev.rollczi.litecommands.command.CommandExecuteResult;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;

public class InvocationResult<SENDER> {

    private final Invocation<SENDER> invocation;
    private final @Nullable CommandExecuteResult commandExecuteResult;
    private final @Nullable FailedReason failedReason;

    public InvocationResult(Invocation<SENDER> invocation, @Nullable CommandExecuteResult commandExecuteResult, @Nullable FailedReason failedReason) {
        this.invocation = invocation;
        this.commandExecuteResult = commandExecuteResult;
        this.failedReason = failedReason;
    }

    public Invocation<SENDER> getInvocation() {
        return invocation;
    }

    public boolean isInvoked() {
        return commandExecuteResult != null;
    }

    public boolean isFailed() {
        return failedReason != null;
    }

    public CommandExecuteResult getCommandExecuteResult() {
        if (commandExecuteResult == null) {
            throw new NoSuchElementException("Command has not been executed!");
        }

        return commandExecuteResult;
    }

    public FailedReason getFailedReason() {
        if (failedReason == null) {
            throw new NoSuchElementException("Command has not been failed!");
        }

        return failedReason;
    }

    public static <SENDER> InvocationResult<SENDER> success(Invocation<SENDER> invocation, CommandExecuteResult executeResult) {
        return new InvocationResult<>(invocation, executeResult, null);
    }

    public static <SENDER> InvocationResult<SENDER> failed(Invocation<SENDER> invocation, FailedReason failedReason) {
        return new InvocationResult<>(invocation, null, failedReason);
    }

}
