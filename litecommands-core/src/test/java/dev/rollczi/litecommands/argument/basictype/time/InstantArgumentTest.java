package dev.rollczi.litecommands.argument.basictype.time;

import dev.rollczi.litecommands.test.TestFactory;
import dev.rollczi.litecommands.test.TestPlatform;
import dev.rollczi.litecommands.test.TestUtils;
import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import dev.rollczi.litecommands.suggestion.Suggestion;
import org.junit.jupiter.api.Test;
import panda.std.Result;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InstantArgumentTest {

    InstantArgument instantArgument;

    InstantArgumentTest() {
        instantArgument = new InstantArgument();
    }

    @Test
    void testParseMultilevel() {
        Result<Instant, ?> result = instantArgument.parseMultilevel(TestUtils.invocation(), "1970-01-01", "00:00:00");

        assertEquals(Instant.parse("1970-01-01T00:00:00.00Z"), result.get());
    }

    @Test
    void testValidate() {
        boolean validate = instantArgument.validate(TestUtils.invocation(), Suggestion.of("1970-01-01 00:00:00"));

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
        testPlatform.execute("command", "1970-01-01", "00:00:00")
            .assertResult(Instant.parse("1970-01-01T00:00:00.00Z"));
    }

    @Test
    void testSuggestionOnPlatform() {
        testPlatform.suggestAsOp("command", "")
            .assertNonEmpty();
    }

}
