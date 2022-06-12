package dev.rollczi.litecommands.argument;

import dev.rollczi.litecommands.TestFactory;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.section.Section;
import dev.rollczi.litecommands.TestPlatform;
import org.junit.jupiter.api.Test;

class PrimitiveTypeTest {

    TestPlatform platform = TestFactory.withCommands(Command.class);

    @Test
    void testInt() {
        platform.assertSuccess("test", "int", "1");
        platform.assertSuccess("test", "int-object", "1");
    }

    @Test
    void testFloat() {
        platform.assertSuccess("test", "float", "0.5F");
        platform.assertSuccess("test", "float-object", "0.5F");
    }

    @Test
    void testDouble() {
        platform.assertSuccess("test", "double", "0.5D");
        platform.assertSuccess("test", "double-object", "0.5D");
    }

    @Test
    void testBoolean() {
        platform.assertSuccess("test", "boolean", "true");
        platform.assertSuccess("test", "boolean-object", "false");
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
