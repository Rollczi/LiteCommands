package dev.rollczi.litecommands;

import dev.rollczi.litecommands.command.execute.ExecuteResult;
import org.junit.jupiter.api.Assertions;

public class AssertResult {

    private final ExecuteResult executeResult;

    public AssertResult(ExecuteResult executeResult) {
        this.executeResult = executeResult;
    }

    public void assertSuccess() {
        Assertions.assertTrue(executeResult.isSuccess());
    }

    public void assertFail() {
        Assertions.assertTrue(executeResult.isFailure());
    }

    public void assertInvalid() {
        Assertions.assertTrue(executeResult.isInvalid());
    }

    public void assertResult(Object result) {
        Assertions.assertEquals(result, executeResult.getResult());
    }

    public void assertNullResult() {
        Assertions.assertNull(executeResult.getResult());
    }

}
