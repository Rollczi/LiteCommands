package dev.rollczi.litecommands.argument;

import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.section.Section;
import dev.rollczi.litecommands.implementation.LiteFactory;
import dev.rollczi.litecommands.implementation.TestHandle;
import dev.rollczi.litecommands.implementation.TestPlatform;
import org.junit.jupiter.api.Test;

class PrimitiveTypeTest {

    private final TestPlatform testPlatform = new TestPlatform();
    private final LiteCommands<TestHandle> liteCommands = LiteFactory.builder(TestHandle.class)
            .command(Command.class)
            .platform(testPlatform)
            .register();

    @Test
    void testInt() {
        testPlatform.execute("test", "int", "1");
        testPlatform.execute("test", "int-object", "1");
    }

    @Test
    void testFloat() {
        testPlatform.execute("test", "float", "0.5F");
        testPlatform.execute("test", "float-object", "0.5F");
    }

    @Test
    void testDouble() {
        testPlatform.execute("test", "double", "0.5D");
        testPlatform.execute("test", "double-object", "0.5D");
    }

    @Test
    void testBoolean() {
        testPlatform.execute("test", "boolean", "true");
        testPlatform.execute("test", "boolean-object", "false");
    }

    @Section(route = "test")
    private static class Command {
        @Execute(route = "int") void test(@Arg int __) {}
        @Execute(route = "int-object") void test(@Arg Integer __) {}
        @Execute(route = "float") void test(@Arg float __) {}
        @Execute(route = "float-object") void test(@Arg Float __) {}
        @Execute(route = "double") void test(@Arg double __) {}
        @Execute(route = "double-object") void test(@Arg Double __) {}
        @Execute(route = "boolean") void test(@Arg boolean __) {}
        @Execute(route = "boolean-object") void test(@Arg Boolean __) {}
    }

}
