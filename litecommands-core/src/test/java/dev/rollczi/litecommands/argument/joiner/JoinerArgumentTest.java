package dev.rollczi.litecommands.argument.joiner;

import dev.rollczi.litecommands.TestFactory;
import dev.rollczi.litecommands.TestPlatform;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class JoinerArgumentTest {

    TestPlatform testPlatform = TestFactory.withCommandsUniversalHandler(Command.class);

    @Route(name = "command")
    static class Command {
        @Execute(route = "default")
        String defaultJoiner(@Joiner String joiner) {
            return joiner;
        }

        @Execute(route = "delimiter")
        String delimiterJoiner(@Joiner(delimiter = ":") String joiner) {
            return joiner;
        }

        @Execute(route = "limited")
        String limitedJoiner(@Joiner(limit = 2) String joiner) {
            return joiner;
        }

        @Execute(route = "delimiter-limited")
        String delimiterLimitedJoiner(@Joiner(delimiter = ":", limit = 2) String joiner) {
            return joiner;
        }
    }

    @CsvSource({
        "default arg-0,arg-0",
        "default arg-0 arg-1,arg-0 arg-1",
        "default arg-0 arg-1 arg-2,arg-0 arg-1 arg-2",

        "delimiter arg-0,arg-0",
        "delimiter arg-0 arg-1,arg-0:arg-1",
        "delimiter arg-0 arg-1 arg-2,arg-0:arg-1:arg-2",

        "limited arg-0,arg-0",
        "limited arg-0 arg-1,arg-0 arg-1",
        "limited arg-0 arg-1 arg-2,arg-0 arg-1",

        "delimiter-limited arg-0,arg-0",
        "delimiter-limited arg-0 arg-1,arg-0:arg-1",
        "delimiter-limited arg-0 arg-1 arg-2,arg-0:arg-1"
    })
    @ParameterizedTest
    void testExecuteWithMixedUnits(String commandArguments, String expectedJoinedText) {
        testPlatform.execute("command", commandArguments.split(" "))
                .assertSuccess()
                .assertResult(expectedJoinedText);
    }

}
