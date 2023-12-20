package dev.rollczi.litecommands.unit;

import dev.rollczi.litecommands.command.executor.CommandExecuteResult;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.permission.MissingPermissions;
import dev.rollczi.litecommands.reflect.LiteCommandsReflectInvocationException;
import org.opentest4j.AssertionFailedError;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class AssertExecute {

    private final CommandExecuteResult result;
    private final Invocation<TestSender> invocation;

    public AssertExecute(CommandExecuteResult result, Invocation<TestSender> invocation) {
        this.result = result;
        this.invocation = invocation;
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

        assertThat(object).isEqualTo(object);

        return this;
    }

    public AssertExecute assertThrows(Class<? extends Throwable> exception) {
        if (result.isFailed()) {
            throw new AssertionError("Command was not successful executed " + result.getError());
        }

        if (result.isSuccessful()) {
            throw new AssertionError("Command was successful executed with result " + result.getResult());
        }

        Throwable throwable = result.getThrowable();

        if (throwable instanceof LiteCommandsReflectInvocationException) {
            throwable = throwable.getCause();
        }

        if (!throwable.getClass().equals(exception)) {
            throw new AssertionFailedError("Command throws different exception: " + throwable.getClass().getName(), throwable);
        }

        return this;
    }

    public AssertExecute assertFailure() {
        if (!result.isFailed()) {
            if (result.isThrown()) {
                throw new AssertionError("Command was thrown", result.getThrowable());
            }

            throw new AssertionError("Command was not failed. Result: " + result.getResult());
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
        assertThat(missingPermissions.getPermissions().toArray(new String[0]))
            .containsOnlyOnce(permissions);

        return this;
    }

    public AssertExecute assertMessage(String message) {
        assertThat(invocation.sender().getMessages())
            .contains(message);
        return this;
    }

}
