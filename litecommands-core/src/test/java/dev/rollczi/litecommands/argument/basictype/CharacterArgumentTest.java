package dev.rollczi.litecommands.argument.basictype;

import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import dev.rollczi.litecommands.suggestion.Suggestion;
import dev.rollczi.litecommands.test.TestFactory;
import dev.rollczi.litecommands.test.TestPlatform;
import dev.rollczi.litecommands.test.TestUtils;
import org.junit.jupiter.api.Test;
import panda.std.Result;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CharacterArgumentTest {

    CharacterArgument characterArgument = new CharacterArgument();

    @Test
    void testParse() {
        Result<Character, ?> result = characterArgument.parse(TestUtils.invocation(), "a");

        assertEquals('a', result.get());
    }

    @Test
    void testValidate() {
        boolean validate = characterArgument.validate(TestUtils.invocation(), Suggestion.of("a"));

        assertTrue(validate);
    }

    TestPlatform testPlatform = TestFactory.withCommandsUniversalHandler(Command.class);

    @Route(name = "command")
    static class Command {

        @Execute(route = "char")
        char execute(@Arg char argument) {
            return argument;
        }

        @Execute(route = "char-class")
        Character executeClass(@Arg Character argument) {
            return argument;
        }

    }

    @Test
    void testExecuteOnPlatform() {
        testPlatform.execute("command", "char", "a")
                .assertResult('a');

        testPlatform.execute("command", "char-class", "b")
                .assertResult('b');
    }

}
