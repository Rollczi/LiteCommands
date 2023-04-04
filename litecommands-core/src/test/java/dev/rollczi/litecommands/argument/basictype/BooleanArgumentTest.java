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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BooleanArgumentTest {

    BooleanArgument argument = new BooleanArgument();

    @Test
    void testParse() {
        Result<Boolean, ?> result = argument.parse(TestUtils.invocation(), "true");
        assertEquals(true, result.get());

        result = argument.parse(TestUtils.invocation(), "false");
        assertEquals(false, result.get());

        result = argument.parse(TestUtils.invocation(), "fa");
        assertTrue(result.isErr());

        result = argument.parse(TestUtils.invocation(), "tr");
        assertTrue(result.isErr());

        result = argument.parse(TestUtils.invocation(), "");
        assertTrue(result.isErr());
    }

    @Test
    void testValidate() {
        boolean validate = argument.validate(TestUtils.invocation(), Suggestion.of("true"));
        assertTrue(validate);

        validate = argument.validate(TestUtils.invocation(), Suggestion.of("false"));
        assertTrue(validate);

        validate = argument.validate(TestUtils.invocation(), Suggestion.of("fa"));
        assertFalse(validate);

        validate = argument.validate(TestUtils.invocation(), Suggestion.of("tr"));
        assertFalse(validate);

        validate = argument.validate(TestUtils.invocation(), Suggestion.of(""));
        assertFalse(validate);
    }

    @Test
    void testSuggestion() {
        List<Suggestion> suggestions = argument.suggest(TestUtils.invocation("command", "test"));
        assertEquals(2, suggestions.size());
    }


    TestPlatform testPlatform = TestFactory.withCommandsUniversalHandler(Command.class);

    @Route(name = "command")
    static class Command {

        @Execute(route = "boolean")
        boolean execute(@Arg boolean argument) {
            return argument;
        }

        @Execute(route = "boolean-class")
        Boolean executeClass(@Arg Boolean argument) {
            return argument;
        }

    }

    @Test
    void testExecuteOnPlatform() {
        testPlatform.execute("command", "boolean", "true")
                .assertResult(true);

        testPlatform.execute("command", "boolean-class", "false")
                .assertResult(false);
    }


}
