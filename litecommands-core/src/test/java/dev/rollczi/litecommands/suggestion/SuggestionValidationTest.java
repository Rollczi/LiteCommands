package dev.rollczi.litecommands.suggestion;

import dev.rollczi.litecommands.test.TestFactory;
import dev.rollczi.litecommands.test.TestPlatform;
import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import org.junit.jupiter.api.Test;

class SuggestionValidationTest {

    TestPlatform platform = TestFactory.withCommands(Command.class);

    @Route(name = "test")
    private static class Command {
        @Execute void test(@Arg int arg0, @Arg int arg1, @Arg String text) {}
    }

    @Test
    void test() {
        platform.suggestAsOp("test", "").assertWith("0", "1", "5", "10", "50", "100", "500");
        platform.suggestAsOp("test", "5").assertWith("5", "50", "500");
        platform.suggestAsOp("test", "55").assertWith( "55");
        platform.suggestAsOp("test", "35", "").assertWith( "0", "1", "5", "10", "50", "100", "500");
        platform.suggestAsOp("test", "text", "").assertWith();
    }

}
