package dev.rollczi.litecommands.test;

import dev.rollczi.litecommands.command.InvalidUsage;
import dev.rollczi.litecommands.command.execute.ExecuteResult;
import dev.rollczi.litecommands.command.permission.RequiredPermissions;
import org.junit.jupiter.api.Assertions;

public class AssertResult {

    private final ExecuteResult executeResult;

    public AssertResult(ExecuteResult executeResult) {
        this.executeResult = executeResult;
    }

    public AssertResult assertSuccess() {
        Assertions.assertTrue(executeResult.isSuccess());
        return this;
    }

    public AssertResult assertFail() {
        Assertions.assertTrue(executeResult.isFailure());
        return this;
    }

    public AssertResult assertInvalid() {
        Assertions.assertTrue(executeResult.isInvalid());
        return this;
    }

    public InvalidUsage assertInvalidUsage() {
        return this.assertInvalid()
            .assertResultIs(InvalidUsage.class);
    }

    public RequiredPermissions assertRequiredPermissions() {
        return this.assertInvalid()
            .assertResultIs(RequiredPermissions.class);
    }

    public AssertResult assertResult(Object result) {
        Assertions.assertEquals(result, executeResult.getResult());
        return this;
    }

    public AssertResult assertMessage(String message) {
        TestHandle testHandle = Assertions.assertInstanceOf(TestHandle.class, executeResult.getBased().getInvocation().handle());
        Assertions.assertTrue(testHandle.containsMessage(message));
        return this;
    }

    public <T> T assertResultIs(Class<T> clazz) {
        return Assertions.assertInstanceOf(clazz, executeResult.getResult());
    }

    public AssertResult assertNullResult() {
        Assertions.assertNull(executeResult.getResult());
        return this;
    }

}
