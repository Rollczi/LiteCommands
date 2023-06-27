package dev.rollczi.litecommands.unit;

import dev.rollczi.litecommands.command.executor.CommandExecuteResult;
import dev.rollczi.litecommands.permission.MissingPermissions;
import org.opentest4j.AssertionFailedError;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class AssertExecute {

    private final CommandExecuteResult result;

    public AssertExecute(CommandExecuteResult result) {
        this.result = result;
    }

    public AssertExecute assertSuccess() {
        if (result.isThrown()) {
            throw new AssertionError("Command was thrown ", result.getThrowable());
        }

        if (result.isFailed()) {
            throw new AssertionError("Command was not successful executed " + result.getError());
        }

        return this;
    }

    public AssertExecute assertSuccess(Object expected) {
        if (result.isThrown()) {
            throw new AssertionError("Command was thrown", result.getThrowable());
        }

        if (result.isFailed()) {
            throw new AssertionError("Command was not successful executed " + result.getError());
        }

        Object object = result.getResult();

        if (object == null) {
            throw new AssertionError("Command result is empty");
        }

        assertEquals(expected, object);

        return this;
    }

    public AssertExecute assertThrows(Class<? extends Throwable> exception) {
        if (result.isFailed()) {
            throw new AssertionError("Command was not successful executed " + result.getError());
        }

        if (result.isSuccessful()) {
            throw new AssertionError("Command was successful executed with result " + result.getResult());
        }

        if (!result.getThrowable().getClass().equals(exception)) {
            throw new AssertionFailedError("Command throws different exception: " + result.getThrowable().getClass().getName());
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

        Object error = result.getError();

        if (error == null) {
            throw new AssertionError("Failed reason is empty");
        }

        return assertInstanceOf(type, error);
    }

    public AssertExecute assertFailure(Object reason) {
        if (!result.isFailed()) {
            throw new AssertionError("Command was not failed.");
        }

        Object error = result.getError();

        if (error == null) {
            throw new AssertionError("Failed reason is empty");
        }

        assertEquals(reason, error);

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
