package dev.rollczi.litecommands.annotations.cases;

import dev.rollczi.litecommands.annotations.LiteTestSpec;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.invalidusage.InvalidUsageException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ThrowInvalidUsageExecutionTest extends LiteTestSpec {

    @Command(name = "test")
    static class TestCommand {

        @Execute
        void execute() {
            throw new InvalidUsageException(InvalidUsage.Cause.INVALID_ARGUMENT);
        }

    }

    @Test
    void test() {
        InvalidUsage invalidUsage = platform.execute("test")
            .assertFailedAs(InvalidUsage.class);

        assertEquals(InvalidUsage.Cause.INVALID_ARGUMENT, invalidUsage.getCause());
    }

}
