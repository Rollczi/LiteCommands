package dev.rollczi.litecommands.argument.basictype.time;

import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import dev.rollczi.litecommands.test.TestFactory;
import dev.rollczi.litecommands.test.TestPlatform;
import dev.rollczi.litecommands.test.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import panda.std.Result;

import java.time.ZoneId;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ZoneIdArgumentTest {

    ZoneIdArgument argument = new ZoneIdArgument();

    @Test
    void testParse() {
        Result<ZoneId, ?> result = argument.parseMultilevel(TestUtils.invocation(), "Europe/Warsaw");

        assertEquals(ZoneId.of("Europe/Warsaw"), result.get());
    }

    @CsvSource({
        "Europe/Warsaw,Europe/Warsaw",
        "Europe/London,Europe/London",
        "Europe/Paris,Europe/Paris"
    })
    @ParameterizedTest
    void testParseWithSingleUnits(String period, String expectedZoneId) {
        Result<ZoneId, ?> result = argument.parseMultilevel(TestUtils.invocation(), period);

        assertEquals(ZoneId.of(expectedZoneId), result.get());
    }

    TestPlatform platform = TestFactory.withCommandsUniversalHandler(Command.class);

    @Route(name = "test")
    public static class Command {

        @Execute
        public ZoneId execute(@Arg ZoneId zoneId) {
            return zoneId;
        }

    }

    @Test
    void testExecuteOnPlatform() {
        platform.execute("test", "Europe/Warsaw")
            .assertSuccess()
            .assertResult(ZoneId.of("Europe/Warsaw"));

        platform.execute("test", "GMT+2")
            .assertSuccess()
            .assertResult(ZoneOffset.ofOffset("GMT", ZoneOffset.ofHours(2)));

        platform.execute("test", "+02:00")
            .assertSuccess()
            .assertResult(ZoneOffset.ofHours(2));
    }

    @Test
    void testSuggestionOnPlatform() {
        platform.suggest("test", "")
            .assertWith(ZoneId.getAvailableZoneIds().toArray(new String[0]));

        platform.suggest("test", "Europe/Warsa")
            .assertWith("Europe/Warsaw");
    }

}
