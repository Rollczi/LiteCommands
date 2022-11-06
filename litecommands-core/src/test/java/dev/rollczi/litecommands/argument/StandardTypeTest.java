package dev.rollczi.litecommands.argument;

import dev.rollczi.litecommands.test.TestFactory;
import dev.rollczi.litecommands.test.TestPlatform;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import org.junit.jupiter.api.Test;

class StandardTypeTest {

    TestPlatform platform = TestFactory.withCommands(Command.class);

    @Route(name = "test")
    private static class Command {
        @Execute(route = "int")             void test(@Arg int __) {}
        @Execute(route = "int-object")      void test(@Arg Integer __) {}

        @Execute(route = "float")           void test(@Arg float __) {}
        @Execute(route = "float-object")    void test(@Arg Float __) {}

        @Execute(route = "double")          void test(@Arg double __) {}
        @Execute(route = "double-object")   void test(@Arg Double __) {}

        @Execute(route = "boolean")         void test(@Arg boolean __) {}
        @Execute(route = "boolean-object")  void test(@Arg Boolean __) {}
    }

    @Test
    void testInt() {
        platform.execute("test", "int", "1").assertSuccess();
        platform.execute("test", "int-object", "1").assertSuccess();
    }

    @Test
    void testFloat() {
        platform.execute("test", "float", "0.5F").assertSuccess();
        platform.execute("test", "float-object", "0.5F").assertSuccess();
    }

    @Test
    void testDouble() {
        platform.execute("test", "double", "0.5D").assertSuccess();
        platform.execute("test", "double-object", "0.5D").assertSuccess();
    }

    @Test
    void testBoolean() {
        platform.execute("test", "boolean", "true").assertSuccess();
        platform.execute("test", "boolean-object", "false").assertSuccess();
    }

}
