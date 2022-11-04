package dev.rollczi.litecommands.suggestion;

import dev.rollczi.litecommands.TestFactory;
import dev.rollczi.litecommands.TestPlatform;
import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import org.junit.jupiter.api.Test;

class SuggestionStringTest {

    TestPlatform platform = TestFactory.withCommands(Command.class);

    @Route(name = "test")
    private static class Command {
        @Execute void test(@Arg String arg) {}
    }

    @Test
    void test() {
        platform.suggest("test", "").assertWith("text");
        platform.suggest("test", "t").assertWith("t", "text");
        platform.suggest("test", "siema").assertWith("siema");
    }

}
