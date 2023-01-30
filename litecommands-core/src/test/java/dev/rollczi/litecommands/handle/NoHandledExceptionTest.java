package dev.rollczi.litecommands.handle;

import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import dev.rollczi.litecommands.test.TestFactory;
import dev.rollczi.litecommands.test.TestPlatform;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class NoHandledExceptionTest {

    TestPlatform platform = TestFactory.withCommands(Command.class);

    @Route(name = "test")
    static class Command {
        @Execute
        void handle() {
            throw new RuntimeException("error");
        }
    }

    @Test
    void test() {
        assertThrows(RuntimeException.class, () -> platform.execute("test"));
    }


}
