package dev.rollczi.litecommands.contextual;

import dev.rollczi.litecommands.AssertResult;
import dev.rollczi.litecommands.TestFactory;
import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.execute.ExecuteResult;
import dev.rollczi.litecommands.command.section.Section;
import dev.rollczi.litecommands.TestPlatform;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContextualTest {

    TestPlatform platform = TestFactory.withCommands(Command.class);

    @Test
    void test() {
        AssertResult result = platform.execute("test", "Rollczi");

        result.assertResult("contextual + Rollczi");
    }

    @Section(route = "test")
    static class Command {
        @Execute
        String execute(String contextual, @Arg String argument) {
            return contextual + " + " + argument;
        }
    }

}
