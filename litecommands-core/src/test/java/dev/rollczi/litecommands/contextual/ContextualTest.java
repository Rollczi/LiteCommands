package dev.rollczi.litecommands.contextual;

import dev.rollczi.litecommands.AssertResult;
import dev.rollczi.litecommands.TestFactory;
import dev.rollczi.litecommands.TestPlatform;
import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import org.junit.jupiter.api.Test;

class ContextualTest {

    TestPlatform platform = TestFactory.withCommands(Command.class);

    @Test
    void test() {
        AssertResult result = platform.execute("test", "Rollczi");

        result.assertResult("contextual + Rollczi");
    }

    @Route(name = "test")
    static class Command {
        @Execute
        String execute(String contextual, @Arg String argument) {
            return contextual + " + " + argument;
        }
    }

}
