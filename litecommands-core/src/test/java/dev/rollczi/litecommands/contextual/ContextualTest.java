package dev.rollczi.litecommands.contextual;

import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.execute.ExecuteResult;
import dev.rollczi.litecommands.command.section.Section;
import dev.rollczi.litecommands.implementation.LiteFactory;
import dev.rollczi.litecommands.implementation.TestHandle;
import dev.rollczi.litecommands.implementation.TestPlatform;
import org.junit.jupiter.api.Test;
import panda.std.Result;

import static org.junit.jupiter.api.Assertions.*;

class ContextualTest {

    private final TestPlatform testPlatform = new TestPlatform();
    private final LiteCommands<TestHandle> liteCommands = LiteFactory.builder(TestHandle.class)
            .platform(testPlatform)
            .argument(String.class, (invocation, argument) -> Result.ok(argument))
            .contextualBind(String.class, (testHandle, invocation) -> Result.ok("contextual"))
            .resultHandler(String.class, (testHandle, invocation, value) -> {})
            .command(Command.class)
            .register();


    @Test
    void test() {
        ExecuteResult result = testPlatform.execute("test", "Rollczi");

        assertEquals("contextual + Rollczi", result.getResult());
    }

    @Section(route = "test")
    private static class Command {

        @Execute
        String execute(String contextual, @Arg String argument) {
            return contextual + " + " + argument;
        }

    }

}
