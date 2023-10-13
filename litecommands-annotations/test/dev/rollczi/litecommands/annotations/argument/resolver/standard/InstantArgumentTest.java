package dev.rollczi.litecommands.annotations.argument.resolver.standard;

import dev.rollczi.litecommands.annotations.LiteTestSpec;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.suggestion.Suggestion;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;

public class InstantArgumentTest extends LiteTestSpec {

    @Command(name = "test")
    static class TestCommand {

        @Execute
        Instant test(@Arg Instant instant) {
            return instant;
        }
    }

    @Test
    void test() {
        platform.execute("test 2023-10-13T15:30:00Z")
            .assertSuccess(Instant.parse("2023-10-13T15:30:00Z"));
    }

    @Test
    void testInvalid() {
        platform.execute("test 1acab")
            .assertFailure();
    }

    @Test
    void testSuggestions() {
        platform.suggest("test ").getSuggestions().stream()
            .map(Suggestion::multilevel)
            .forEach(suggestion -> platform.execute("test " + suggestion).assertSuccess());
    }
}
