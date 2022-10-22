package dev.rollczi.litecommands.argument.basictype;

import dev.rollczi.litecommands.TestFactory;
import dev.rollczi.litecommands.TestPlatform;
import dev.rollczi.litecommands.TestUtils;
import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.section.Section;
import dev.rollczi.litecommands.suggestion.Suggestion;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import panda.std.Result;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DurationArgumentTest {

    DurationArgument durationArgument = new DurationArgument();

    @Test
    void testParse() {
        Result<Duration, ?> result = durationArgument.parse(TestUtils.invocation(), "1m20s");

        assertEquals(Duration.ofMinutes(1).plus(20, ChronoUnit.SECONDS), result.get());
    }

    @CsvSource({
        "4d3h1m,356460",
        "1d2h5m30s,93930",
        "2d10h0m20s,208820"
    })
    @ParameterizedTest
    void testParseWithMixedUnits(String period, int expectedSecondsCount) {
        Result<Duration, ?> result = durationArgument.parse(TestUtils.invocation(), period);

        assertEquals(Duration.ofSeconds(expectedSecondsCount), result.get());
    }

    @CsvSource({
        "1d,86400",
        "1h,3600",
        "1m,60"
    })
    @ParameterizedTest
    void testParseWithSingleUnits(String period, int expectedSecondsCount) {
        Result<Duration, ?> result = durationArgument.parse(TestUtils.invocation(), period);

        assertEquals(Duration.ofSeconds(expectedSecondsCount), result.get());
    }

    @Test
    void testValidate() {
        boolean validate = durationArgument.validate(TestUtils.invocation(), Suggestion.of("1d10m"));

        assertTrue(validate);
    }

    TestPlatform testPlatform = TestFactory.withCommandsUniversalHandler(Command.class);

    @Section(route = "command")
    static class Command {
        @Execute
        Duration execute(@Arg Duration argument) {
            return argument;
        }
    }

    @Test
    void testExecuteOnPlatform() {
        testPlatform.execute("command", "10s")
                .assertResult(Duration.ofSeconds(10));

        testPlatform.execute("command", "10d3h20m56s")
                .assertResult(Duration.ofDays(10)
                        .plus(3, ChronoUnit.HOURS)
                        .plus(20, ChronoUnit.MINUTES)
                        .plus(56, ChronoUnit.SECONDS)
                );
    }

    @Test
    void testSuggestionOnPlatform() {
        testPlatform.suggest("command", "")
                .assertWith(TypeUtils.DURATION_SUGGESTION);

        testPlatform.suggest("command", "1m")
                .assertWith("1m", "1m30s");
    }

}
