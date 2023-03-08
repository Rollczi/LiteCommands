package dev.rollczi.litecommands.modern.annotation.route;

import dev.rollczi.litecommands.modern.annotation.argument.Arg;
import dev.rollczi.litecommands.modern.annotation.execute.Execute;
import dev.rollczi.litecommands.modern.permission.annotation.Permission;
import dev.rollczi.litecommands.modern.test.LiteTest;
import dev.rollczi.litecommands.modern.test.LiteTestSpec;
import org.junit.jupiter.api.Test;

@LiteTest
class RootRouteTest extends LiteTestSpec {

    @RootRoute
    static class Command {
        @Execute(name = "first")
        public void test() {}

        @Execute(name = "second")
        public void test2() {}
    }

    @RootRoute
    @Permission("test.permission")
    static class Command2 {
        @Execute(name = "first")
        @Permission("test.permission.execute")
        public void test(@Arg String test) {}

        @Execute(name = "third")
        public void test2() {}
    }

    @Test
    void testExecuteRootRouteCommands() {;
        platform.execute("first")
            .assertMissingPermission("test.permission");

        platform.execute("second")
            .assertSuccessful();
    }

    @Test
    void testExecuteMergedRootRouteCommands() {
        platform.execute("first test")
            .assertMissingPermission("test.permission", "test.permission.execute");

        platform.execute("third")
            .assertMissingPermission("test.permission");
    }

}