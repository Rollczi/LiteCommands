package dev.rollczi.litecommands.argument.basictype;

import dev.rollczi.litecommands.TestFactory;
import dev.rollczi.litecommands.TestPlatform;
import dev.rollczi.litecommands.TestUtils;
import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import dev.rollczi.litecommands.suggestion.Suggestion;
import org.junit.jupiter.api.Test;
import panda.std.Result;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InstantArgumentTest {

    InstantArgument instantArgument;

    InstantArgumentTest() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        instantArgument = new InstantArgument(simpleDateFormat);
    }

    @Test
    void testParseMultilevel() {
        Result<Instant, ?> result = instantArgument.parseMultilevel(TestUtils.invocation(), "00:00", "01/01/1970");

        assertEquals(Instant.parse("1970-01-01T00:00:00.00Z"), result.get());
    }

    @Test
    void testValidate() {
        boolean validate = instantArgument.validate(TestUtils.invocation(), Suggestion.of("00:00 01/01/1970"));

        assertTrue(validate);
    }

    @Test
    void testValidateWithMissingArgument() {
        boolean validate = instantArgument.validate(TestUtils.invocation(), Suggestion.of("00:00"));

        assertFalse(validate);
    }

    @Test
    void testValidateWithInvalidFormat() {
        boolean validate = instantArgument.validate(TestUtils.invocation(), Suggestion.of("00:00 01.01.1970"));

        assertFalse(validate);
    }

    TestPlatform testPlatform = TestFactory.withCommandsUniversalHandler(Command.class);

    @Route(name = "command")
    static class Command {
        @Execute
        Instant execute(@Arg Instant argument) {
            return argument;
        }
    }

    @Test
    void testExecuteOnPlatform() {
        testPlatform.execute("command", "00:00", "01/01/1970")
            .assertResult(Instant.parse("1970-01-01T00:00:00.00Z"));
    }

    @Test
    void testSuggestionOnPlatform() {
        testPlatform.suggest("command", "")
            .assertNonEmpty();
    }

}
