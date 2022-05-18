package dev.rollczi.litecommands.implementation;

import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.execute.ExecuteResult;
import dev.rollczi.litecommands.command.section.Section;
import org.junit.jupiter.api.Test;
import panda.std.Result;

import static org.junit.jupiter.api.Assertions.*;

class TestExampleInfoCommand {

    private final TestPlatform testPlatform = new TestPlatform();
    private final LiteCommands<TestHandle> liteCommands = LiteFactory.builder(TestHandle.class)
            .platform(testPlatform)
            .command(InfoCommand.class)
            .resultHandler(String.class, (v, invocation, value) -> {})
            .argument(String.class, (invocation, argument) -> Result.when(argument.equals("yes"), argument, "error"))
            .register();


    @Test
    void executeZeroArguments() {
        ExecuteResult result = testPlatform.execute("info", "player");

        assertEquals("player", result.getResult());
    }

    @Test
    void executeOneArgument() {
        ExecuteResult result = testPlatform.execute("info", "player", "yes");

        assertEquals("info -> yes", result.getResult());
    }

    @Test
    void executeOneInvalidArgument() {
        ExecuteResult result = testPlatform.execute("info", "player", "no");

        assertEquals("error", result.getResult());
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
