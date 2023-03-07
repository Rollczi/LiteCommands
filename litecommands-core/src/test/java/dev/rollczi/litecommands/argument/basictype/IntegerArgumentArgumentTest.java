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

class IntegerArgumentArgumentTest {

    IntegerArgument integerArgument = new IntegerArgument();

    @Test
    void testParse() {
        Result<Integer, ?> result = this.integerArgument.parse(TestUtils.invocation(), "100");

        assertEquals(100, result.get());
    }

    @Test
    void testValidate() {
        boolean validate = this.integerArgument.validate(TestUtils.invocation(), Suggestion.of("123"));

        assertTrue(validate);
    }

    TestPlatform testPlatform = TestFactory.withCommandsUniversalHandler(Command.class);

    @Route(name = "command")
    static class Command {

        @Execute(route = "int")
        int execute(@Arg int argument) {
            return argument;
        }

        @Execute(route = "int-class")
        Integer executeClass(@Arg Integer argument) {
            return argument;
        }

    }

    @Test
    void testExecuteOnPlatform() {
        this.testPlatform.execute("command", "int", "1.5")
            .assertNullResult();

        this.testPlatform.execute("command", "int", "5")
            .assertResult(5);

        this.testPlatform.execute("command", "int-class", "10")
            .assertResult(10);
    }

    @Test
    void testSuggestionOnPlatform() {
        this.testPlatform.suggestAsOp("command", "int", "")
            .assertWith(TypeUtils.NUMBER_SUGGESTION);

        this.testPlatform.suggestAsOp("command", "int", "23")
            .assertWith("23");
    }

}
