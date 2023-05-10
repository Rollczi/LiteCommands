package dev.rollczi.litecommands.suggestion;

import dev.rollczi.litecommands.test.TestFactory;
import dev.rollczi.litecommands.test.TestPlatform;
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
        
        platform.suggestAsOp("test", "").assertWith();
        platform.suggestAsOp("test", "t").assertWith("t");
        platform.suggestAsOp("test", "siema").assertWith("siema");
    }

}
