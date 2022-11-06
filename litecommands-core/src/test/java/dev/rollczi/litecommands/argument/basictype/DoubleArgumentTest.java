package dev.rollczi.litecommands.argument.basictype;

import dev.rollczi.litecommands.test.TestFactory;
import dev.rollczi.litecommands.test.TestPlatform;
import dev.rollczi.litecommands.test.TestUtils;
import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import dev.rollczi.litecommands.suggestion.Suggestion;
import org.junit.jupiter.api.Test;
import panda.std.Result;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DoubleArgumentTest {

    DoubleArgument doubleArgument = new DoubleArgument();

    @Test
    void testParse() {
        Result<Double, ?> result = doubleArgument.parse(TestUtils.invocation(), "100.4");

        assertEquals(100.4, result.get());
    }

    @Test
    void testValidate() {
        boolean validate = doubleArgument.validate(TestUtils.invocation(), Suggestion.of("100.4"));

        assertTrue(validate);
    }

    TestPlatform testPlatform = TestFactory.withCommandsUniversalHandler(Command.class);

    @Route(name = "command")
    static class Command {

        @Execute(route = "double")
        double execute(@Arg double argument) {
            return argument;
        }

        @Execute(route = "double-class")
        Double executeClass(@Arg Double argument) {
            return argument;
        }

    }

    @Test
    void testExecuteOnPlatform() {
        testPlatform.execute("command", "double", "1.5")
                .assertResult(1.5D);

        testPlatform.execute("command", "double-class", "10.5")
                .assertResult(10.5D);
    }

    @Test
    void testSuggestionOnPlatform() {
        testPlatform.suggest("command", "double-class", "")
                .assertWith(TypeUtils.DECIMAL_SUGGESTION);

        testPlatform.suggest("command", "double-class", "10.5")
                .assertWith("10.5");
    }

}
