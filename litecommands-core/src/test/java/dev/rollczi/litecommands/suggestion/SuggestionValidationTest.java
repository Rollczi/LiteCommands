package dev.rollczi.litecommands.suggestion;

import dev.rollczi.litecommands.TestFactory;
import dev.rollczi.litecommands.TestPlatform;
import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.section.Section;
import org.junit.jupiter.api.Test;

class SuggestionValidationTest {

    TestPlatform platform = TestFactory.withCommands(Command.class);

    @Section(route = "test")
    private static class Command {
        @Execute void test(@Arg int arg0, @Arg int arg1, @Arg String text) {}
    }

    @Test
    void test() {
        platform.suggest("test", "").assertWith("0", "1", "5", "10", "50", "100", "500");
        platform.suggest("test", "5").assertWith("5", "50", "500");
        platform.suggest("test", "35", "").assertWith("0", "1", "5", "10", "50", "100", "500");
        platform.suggest("test", "text", "").assertWith();
    }

}
