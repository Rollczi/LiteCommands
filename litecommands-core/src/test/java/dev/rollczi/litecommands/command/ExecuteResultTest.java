package dev.rollczi.litecommands.command;

import dev.rollczi.litecommands.AssertResult;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.TestHandle;
import dev.rollczi.litecommands.TestPlatform;
import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.section.Section;
import dev.rollczi.litecommands.implementation.LiteFactory;
import org.junit.jupiter.api.Test;
import panda.std.Result;

class ExecuteResultTest {

    private final TestPlatform testPlatform = new TestPlatform();
    private final LiteCommands<TestHandle> liteCommands = LiteFactory.builder(TestHandle.class)
            .platform(testPlatform)
            .command(InfoCommand.class)
            .resultHandler(String.class, (v, invocation, value) -> {})
            .argument(String.class, (invocation, argument) -> Result.when(argument.equals("yes"), argument, "error"))
            .register();


    @Test
    void executeZeroArguments() {
        AssertResult result = testPlatform.execute("info", "player");

        result.assertResult("player");
    }

    @Test
    void executeOneArgument() {
        AssertResult result = testPlatform.execute("info", "player", "yes");

        result.assertResult("info -> yes");
    }

    @Test
    void executeOneInvalidArgument() {
        AssertResult result = testPlatform.execute("info", "player", "no");

        result.assertResult("error");
    }

    @Section(route = "info")
    static class InfoCommand {
        @Execute(route = "player", required = 0)
        String execute() {
            return "player";
        }
        @Execute(route = "player", required = 1)
        String execute(@Arg String player) {
            return "info -> " + player;
        }
    }

}
