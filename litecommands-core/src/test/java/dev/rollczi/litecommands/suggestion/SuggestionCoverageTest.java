package dev.rollczi.litecommands.suggestion;

import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import dev.rollczi.litecommands.test.TestFactory;
import dev.rollczi.litecommands.test.TestPlatform;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

class SuggestionCoverageTest {

    TestPlatform testPlatform = TestFactory.withCommands(Command.class);

    @Route(name = "command")
    static class Command {
        @Execute(route = "LocalDate")
        void test(@Arg LocalDate localDate) {}

        @Execute(route = "LocalDateTime")
        void test(@Arg LocalDateTime localDateTime) {}
    }

    @Test
    void testLocalDate() {
        List<String> suggestion = testPlatform.suggestion("command", "LocalDate", "");

        for (String suggest : suggestion) {
            assertPattern(suggest, "[0-9]{4}-[0-9]{2}-[0-9]{2}");
        }
    }

    private void assertPattern(String suggest, String pattern) {
        if (!suggest.matches(pattern)) {
            throw new AssertionError("Suggestion '" + suggest + "' does not match pattern '" + pattern + "'");
        }
    }

}
