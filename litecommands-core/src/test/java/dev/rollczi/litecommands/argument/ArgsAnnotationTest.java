package dev.rollczi.litecommands.argument;

import dev.rollczi.litecommands.AssertResult;
import dev.rollczi.litecommands.TestFactory;
import dev.rollczi.litecommands.TestPlatform;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.section.Section;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class ArgsAnnotationTest {

    TestPlatform platform = TestFactory.withCommandsUniversalHandler(Command.class, CommandList.class);

    @Section(route = "test-primitive")
    private static class Command {
        @Execute
        String[] test(@Args String[] arguments) {
            return arguments;
        }
    }

    @Section(route = "test-list")
    private static class CommandList {
        @Execute
        List<String> test(@Args List<String> arguments) {
            return arguments;
        }
    }

    @Test
    void testPrimitive() {
        AssertResult success = platform.execute("test-primitive", "1", "2")
                .assertSuccess();

        String[] result = success
                .assertResultIs(String[].class);

        assertArrayEquals(new String[]{"1", "2"}, result);
    }

    @Test
    void testList() {
        platform.execute("test-list", "1", "2")
                .assertSuccess()
                .assertResult(Arrays.asList("1", "2"));
    }

}
