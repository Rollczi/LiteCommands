package dev.rollczi.litecommands.argument.basictype;

import dev.rollczi.litecommands.TestFactory;
import dev.rollczi.litecommands.TestPlatform;
import dev.rollczi.litecommands.TestUtils;
import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.section.Section;
import dev.rollczi.litecommands.suggestion.Suggestion;
import org.junit.jupiter.api.Test;
import panda.std.Result;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IntegerArgumentTest {

    IntegerArgument integerArgument = new IntegerArgument();

    @Test
    void testParse() {
        Result<Integer, ?> result = integerArgument.parse(TestUtils.invocation(), "100");

        assertEquals(100, result.get());
    }

    @Test
    void testValidate() {
        boolean validate = integerArgument.validate(TestUtils.invocation(), Suggestion.of("123"));

        assertTrue(validate);
    }

    TestPlatform testPlatform = TestFactory.withCommandsUniversalHandler(Command.class);

    @Section(route = "command")
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
        testPlatform.execute("command", "int", "1.5")
                .assertNullResult();

        testPlatform.execute("command", "int", "5")
                .assertResult(5);

        testPlatform.execute("command", "int-class", "10")
                .assertResult(10);
    }

    @Test
    void testSuggestionOnPlatform() {
        testPlatform.suggest("command", "int", "")
                .assertWith(TypeUtils.NUMBER_SUGGESTION);

        testPlatform.suggest("command", "int", "10.5")
                .assertWith("10.5");
    }

}
