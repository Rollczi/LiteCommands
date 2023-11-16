package dev.rollczi.litecommands;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.unit.LiteCommandsTestFactory;
import dev.rollczi.litecommands.unit.TestPlatform;
import org.junit.jupiter.api.Test;

class LiteCommandsBuilderTest {

    @Command(name = "first")
    static class FirstCommand {
        @Execute
        void execute() {}
    }

    @Command(name = "second")
    static class SecondCommand {
        @Execute
        void execute() {}
    }

    @Test
    void test() {
        TestPlatform platform = LiteCommandsTestFactory.startPlatform(commandsBuilder -> commandsBuilder
            .commands(new FirstCommand(), new SecondCommand())
        );

        platform.execute("first")
            .assertSuccess();

        platform.execute("second")
            .assertSuccess();
    }

}