package dev.rollczi.litecommands;

import dev.rollczi.litecommands.command.execute.ExecuteResult;
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

    public AssertResult assertResult(Object result) {
        Assertions.assertEquals(result, executeResult.getResult());
        return this;
    }

    public <T> T assertResultIs(Class<T> clazz) {
        Assertions.assertTrue(clazz.isInstance(executeResult.getResult()));
        return clazz.cast(executeResult.getResult());
    }

    public AssertResult assertNullResult() {
        Assertions.assertNull(executeResult.getResult());
        return this;
    }

}
