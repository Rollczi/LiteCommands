package dev.rollczi.litecommands.modern.test.env;

import dev.rollczi.litecommands.modern.argument.FailedReason;
import dev.rollczi.litecommands.modern.command.CommandExecuteResult;
import dev.rollczi.litecommands.modern.invocation.InvocationResult;
import org.opentest4j.AssertionFailedError;
import panda.std.Option;

public class AssertExecute {

    private final InvocationResult<FakeSender> result;

    public AssertExecute(InvocationResult<FakeSender> result) {
        this.result = result;
    }

    public AssertExecute assertSuccessful() {
        if (!result.isInvoked()) {
            throw new AssertionError("Command was not invoked. Reason: " + result.getFailedReason());
        }

        CommandExecuteResult executeResult = result.getCommandExecuteResult();

        if (!executeResult.isSuccessful()) {
            throw new AssertionError("Command was not successful executed", executeResult.getException());
        }

        return this;
    }

    public AssertExecute assertSuccessful(Object result) {
        if (!this.result.isInvoked()) {
            throw new AssertionError("Command was not invoked. Reason: " + this.result.getFailedReason());
        }

        CommandExecuteResult executeResult = this.result.getCommandExecuteResult();

        if (!executeResult.isSuccessful()) {
            throw new AssertionError("Command was not successful executed", executeResult.getException());
        }

        Option<Object> option = executeResult.getResult();

        if (option.isEmpty()) {
            throw new AssertionError("Command result is empty");
        }

        if (!option.get().equals(result)) {
            throw new AssertionFailedError("Command result is not equals", result, option.get());
        }

        return this;
    }

    public AssertExecute assertThrows(Class<? extends Throwable> exception) {
        if (!result.isInvoked()) {
            throw new AssertionError("Command was not invoked. Reason: " + result.getFailedReason());
        }

        CommandExecuteResult executeResult = result.getCommandExecuteResult();

        if (executeResult.isSuccessful()) {
            throw new AssertionError("Command was successful executed");
        }

        if (!executeResult.getException().getClass().equals(exception)) {
            throw new AssertionFailedError("Command throws different exception", exception, executeResult.getException().getClass());
        }

        return this;
    }

    public AssertExecute assertFailed() {
        if (!result.isFailed()) {
            throw new AssertionError("Command was not failed.");
        }

        return this;
    }

    public <T> T assertFailedAs(Class<T> type) {
        if (!result.isFailed()) {
            throw new AssertionError("Command was not failed.");
        }

        FailedReason failedReason = result.getFailedReason();

        if (failedReason.isEmpty()) {
            throw new AssertionError("Failed reason is empty");
        }

        if (!type.isInstance(failedReason.getReason())) {
            throw new AssertionFailedError("Failed reason is not instance of " + type, type, failedReason.getReason().getClass());
        }

        return type.cast(failedReason.getReason());
    }

    public AssertExecute assertFailed(Object reason) {
        if (!result.isFailed()) {
            throw new AssertionError("Command was not failed.");
        }

        FailedReason failedReason = result.getFailedReason();

        if (failedReason.isEmpty()) {
            throw new AssertionError("Failed reason is empty");
        }

        if (!failedReason.getReason().equals(reason)) {
            throw new AssertionFailedError("Failed reason is not equals", reason, failedReason.getReason());
        }

        return this;
    }

}
