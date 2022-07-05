package dev.rollczi.litecommands.scheme;

import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.ArgumentName;
import dev.rollczi.litecommands.argument.By;
import dev.rollczi.litecommands.argument.Name;
import dev.rollczi.litecommands.argument.option.Opt;
import dev.rollczi.litecommands.argument.simple.MultilevelArgument;
import dev.rollczi.litecommands.argument.simple.OneArgument;
import dev.rollczi.litecommands.command.FindResult;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.section.Section;
import dev.rollczi.litecommands.implementation.LiteFactory;
import dev.rollczi.litecommands.TestHandle;
import dev.rollczi.litecommands.TestPlatform;
import org.junit.jupiter.api.Test;
import panda.std.Option;
import panda.std.Result;

import java.util.Arrays;
import java.util.List;

import static dev.rollczi.litecommands.Assert.assertCollection;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SchematicTest {

    private final TestPlatform testPlatform = new TestPlatform();
    private final LiteCommands<TestHandle> liteCommands = LiteFactory.builder(TestHandle.class)
            .platform(testPlatform)
            .argument(String.class, new StringArg())
            .argumentMultilevel(String.class, "loc", new LocationArg())
            .command(Command.class)
            .register();

    @Test
    void schemeBeforeExecutorsTest() {
        FindResult<TestHandle> result = testPlatform.find("teleport");
        SimpleSchemeGenerator schemeGenerator = new SimpleSchemeGenerator();

        List<String> schemes = schemeGenerator.generate(result, SchemeFormat.ARGUMENT_ANGLED_OPTIONAL_SQUARE);

        assertCollection(4, Arrays.asList(
                "/teleport test <target> [to]",
                "/teleport <target> [to]",
                "/teleport <x y z> [world]",
                "/teleport class test <target>"
        ), schemes);
    }

    @Test
    void testMatchedNormal() {
        FindResult<TestHandle> result = testPlatform.find("teleport", "test");
        SimpleSchemeGenerator schemeGenerator = new SimpleSchemeGenerator();

        List<String> schemes = schemeGenerator.generate(result, SchemeFormat.ARGUMENT_ANGLED_OPTIONAL_SQUARE);

        assertEquals(1, schemes.size());
        assertEquals("/teleport <target> [to]", schemes.get(0));
    }

    @Test
    void testMatchedLocation() {
        FindResult<TestHandle> result = testPlatform.find("teleport", "100", "100", "100", "world");
        SimpleSchemeGenerator schemeGenerator = new SimpleSchemeGenerator();

        List<String> schemes = schemeGenerator.generate(result, SchemeFormat.ARGUMENT_ANGLED_OPTIONAL_SQUARE);

        assertEquals(1, schemes.size());
        assertEquals("/teleport <x y z> [world]", schemes.get(0));
    }

    @Section(route = "teleport")
    static class Command {
        @Execute(min = 3, max = 4)
        void toLocation(@Arg @By("loc") String text, @Opt @Name("world") Option<String> world) {}
        @Execute(min = 1, max = 2)
        void targetToPlayer(@Arg @Name("target") String target, @Opt @Name("to") Option<String> to) {}
        @Execute(route = "test")
        void test(@Arg @Name("target") String target, @Opt @Name("to") Option<String> to) {}
        @Section(route = "class")
        static class In {
            @Execute(route = "test")
            void test(@Arg @Name("target") String target) {}
        }
    }

    @ArgumentName("player")
    static class StringArg implements OneArgument<String> {
        @Override
        public Result<String, ?> parse(LiteInvocation invocation, String argument) {
            return Result.ok(argument);
        }
    }

    @ArgumentName("x y z")
    static class LocationArg implements MultilevelArgument<String> {
        @Override
        public Result<String, ?> parseMultilevel(LiteInvocation invocation, String... arguments) {
            return Result.ok(Arrays.toString(arguments));
        }

        @Override
        public int countMultilevel() {
            return 3;
        }
    }

}
