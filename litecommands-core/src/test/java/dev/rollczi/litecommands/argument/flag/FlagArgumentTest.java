package dev.rollczi.litecommands.argument.flag;

import dev.rollczi.litecommands.test.TestFactory;
import dev.rollczi.litecommands.test.TestPlatform;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import org.junit.jupiter.api.Test;

class FlagArgumentTest {

    TestPlatform platform = TestFactory.withCommands(Command.class);

    @Route(name = "test")
    static class Command {
        @Execute
        boolean test(@Flag("-f") boolean flag) {
            return flag;
        }
    }

    @Test
    void shouldReturnTrueIfFlagIsExist() {
        platform.execute("test", "-f").assertResult(true);
    }

    @Test
    void shouldReturnFalseIfFlagIsNoExist() {
        platform.execute("test").assertResult(false);
    }

    @Test
    void shouldReturnFalseIfFlagIsInvalid() {
        platform.execute("test", "-g").assertResult(false);
    }

}
