package dev.rollczi.litecommands.annotation.route;

import dev.rollczi.litecommands.annotation.LiteTest;
import dev.rollczi.litecommands.annotation.LiteTestSpec;
import dev.rollczi.litecommands.annotation.argument.Arg;
import dev.rollczi.litecommands.annotation.execute.Execute;
import dev.rollczi.litecommands.annotation.permission.Permission;
import org.junit.jupiter.api.Test;

@LiteTest
class RootRouteTest extends LiteTestSpec {

    @RootRoute
    static class Command {
        @Execute(route = "first")
        public void test() {}

        @Execute(route = "second")
        public void test2() {}
    }

    @RootRoute
    @Permission("test.permission")
    static class Command2 {
        @Execute(route = "first")
        @Permission("test.permission.execute")
        public void test(@Arg String test) {}

        @Execute(route = "third")
        public void test2() {}
    }

    @Test
    void testExecuteRootRouteCommands() {
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