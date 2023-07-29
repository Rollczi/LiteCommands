package dev.rollczi.litecommands.suggestion;

import dev.rollczi.litecommands.test.TestFactory;
import dev.rollczi.litecommands.test.TestPlatform;
import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import org.junit.jupiter.api.Test;

class SuggestionStringTest {

    TestPlatform platform = TestFactory.withCommands(Command.class, CommandStatic.class);

    @Route(name = "test")
    private static class Command {
        @Execute void test(@Arg String arg) {}
    }

    @Route(name = "testStatic")
    private static class CommandStatic {
        @Execute void test(@Arg @Suggest("static") String arg) {}
    }

    @Test
    void test() {
        
        platform.suggestAsOp("test", "").assertWith();
        platform.suggestAsOp("test", "t").assertWith("t");
        platform.suggestAsOp("test", "siema").assertWith("siema");

        platform.suggestAsOp("testStatic", "").assertWith("static");
        platform.suggestAsOp("testStatic", "s").assertWith("s", "static");
    }

}
