package dev.rollczi.litecommands.annotations.permission;

import dev.rollczi.litecommands.annotations.LiteTestSpec;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import static dev.rollczi.litecommands.unit.TestPlatformSender.*;
import org.junit.jupiter.api.Test;

class PermissionAnnotationSuggestionTest extends LiteTestSpec {

    @Command(name = "test")
    @Permission("test.permission")
    static class TestCommand {

        @Execute(name = "sub")
        @Permission("test.permission.execute")
        void execute() {}

    }

    @Test
    void test() {
         platform.suggest("test")
             .assertSuggest();

        platform.suggest(permitted("test.permission"), "test")
            .assertSuggest("test");
    }


    @Test
    void testSub() {
        platform.suggest(permitted("test.permission"), "test ")
            .assertSuggest();

        platform.suggest(permitted("test.permission", "test.permission.execute"), "test ")
            .assertSuggest("sub");

        platform.suggest(permitted("test.permission.execute"), "test ")
            .assertSuggest();
    }

}
