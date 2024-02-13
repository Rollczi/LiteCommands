package dev.rollczi.litecommands.programmatic;

import dev.rollczi.litecommands.command.executor.LiteContext;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.invalidusage.InvalidUsageException;
import dev.rollczi.litecommands.unit.LiteCommandsTestFactory;
import dev.rollczi.litecommands.unit.TestPlatform;
import dev.rollczi.litecommands.unit.TestSender;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.junit.jupiter.api.Test;

class ThrowInvalidUsageExceptionTest {


    TestPlatform testPlatform = LiteCommandsTestFactory.startPlatform(builder -> builder.commands(
        new LiteCommand<TestSender>("throw")
            .executeReturn(context -> {
                throw new InvalidUsageException(InvalidUsage.Cause.INVALID_ARGUMENT);
            }),

        new LiteCommand<TestSender>("throw-object") {
            @Override
            protected void execute(LiteContext<TestSender> context) {
                throw new InvalidUsageException(InvalidUsage.Cause.INVALID_ARGUMENT);
            }
        }
    ));

    @Test
    public void testSuccess() {
        InvalidUsage usage = testPlatform.execute("throw")
            .assertFailedAs(InvalidUsage.class);

        assertThat(usage.getCause())
            .isEqualTo(InvalidUsage.Cause.INVALID_ARGUMENT);

        usage = testPlatform.execute("throw-object")
            .assertFailedAs(InvalidUsage.class);

        assertThat(usage.getCause())
            .isEqualTo(InvalidUsage.Cause.INVALID_ARGUMENT);
    }

}
