package dev.rollczi.litecommands.command;

import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import dev.rollczi.litecommands.handle.LiteException;
import dev.rollczi.litecommands.test.TestFactory;
import dev.rollczi.litecommands.test.TestPlatform;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class InvalidUsageTest {

    TestPlatform platform = TestFactory.withCommandsUniversalHandler(Command.class);

    @Route(name = "command")
    static class Command {
        @Execute
        void execute(@Arg boolean isInvalid) {
            if (isInvalid) {
                throw LiteException.newInvalidUsage();
            }
        }
    }

    @Test
    void testThrow() {
        platform.execute("command", "false")
            .assertSuccess();

        LiteException liteException = platform.execute("command", "true")
            .assertResultIs(LiteException.class);

        assertInstanceOf(InvalidUsage.class, liteException.getResult());
    }

}