package dev.rollczi.litecommands.unit;

import dev.rollczi.litecommands.argument.FailedReason;
import dev.rollczi.litecommands.command.CommandExecuteResult;
import dev.rollczi.litecommands.invocation.InvocationResult;
import dev.rollczi.litecommands.permission.MissingPermissions;
import org.opentest4j.AssertionFailedError;
import panda.std.Option;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AssertExecute {

    private final InvocationResult<TestSender> result;

    public AssertExecute(InvocationResult<TestSender> result) {
        this.result = result;
    }

    public AssertExecute assertSuccess() {
        if (!result.isInvoked()) {
            throw new AssertionError("Command was not invoked. Reason: " + result.getFailedReason());
        }

        CommandExecuteResult executeResult = result.getCommandExecuteResult();

        if (!executeResult.isSuccessful()) {
            throw new AssertionError("Command was not successful executed", executeResult.getException());
        }

        return this;
    }

    public AssertExecute assertSuccess(Object result) {
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

        assertEquals(result, option.get());

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

    public AssertExecute assertFailure() {
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

    public AssertExecute assertFailure(Object reason) {
        if (!result.isFailed()) {
            throw new AssertionError("Command was not failed.");
        }

        FailedReason failedReason = result.getFailedReason();

        if (failedReason.isEmpty()) {
            throw new AssertionError("Failed reason is empty");
        }

        assertEquals(reason, failedReason.getReason());

        return this;
    }

    public AssertExecute assertMissingPermission(String... permissions) {
        MissingPermissions missingPermissions = assertFailedAs(MissingPermissions.class);

        assertEquals(permissions.length, missingPermissions.getPermissions().size(), "Missing permissions size is not equals");

        for (String permission : permissions) {
            if (!missingPermissions.getPermissions().contains(permission)) {
                throw new AssertionFailedError("Missing permissions does not contains " + permission);
            }
        }

        return this;
    }

}
