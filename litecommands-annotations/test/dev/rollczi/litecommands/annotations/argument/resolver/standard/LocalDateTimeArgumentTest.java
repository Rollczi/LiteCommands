package dev.rollczi.litecommands.annotations.argument.resolver.standard;

import dev.rollczi.litecommands.annotations.LiteTestSpec;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

class LocalDateTimeArgumentTest extends LiteTestSpec {

    @Command(name = "test")
    static class TestCommand {

        @Execute
        LocalDateTime test(@Arg LocalDateTime localDateTime) {
            return localDateTime;
        }
    }

    @Test
    void test() {
        platform.execute("test 2023-10-13 11:20:00")
            .assertSuccess(LocalDateTime.of(2023, 10, 13, 11, 20, 0));
    }

    @Test
    void testInvalid() {
        platform.execute("test invalid")
            .assertFailure();

        platform.execute("test invalid 11:20:00")
            .assertFailure();

        platform.execute("test 2023-10-13 invalid")
            .assertFailure();
    }

    @Test
    void testSuggestions() {
        platform.suggest("test ")
            .assertNotEmpty()
            .assertCorrect(suggestion -> platform.execute("test " + suggestion.multilevel()).assertSuccess());
    }
}
