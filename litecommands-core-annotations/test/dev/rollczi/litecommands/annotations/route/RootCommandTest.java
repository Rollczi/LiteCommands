package dev.rollczi.litecommands.annotations.route;

import dev.rollczi.litecommands.annotations.LiteTestSpec;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.RootCommand;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.junit.jupiter.api.Test;

class RootCommandTest extends LiteTestSpec {

    @RootCommand
    static class Command {
        @Execute(route = "first")
        public void test() {}

        @Execute(route = "second")
        public void test2() {}
    }

    @RootCommand
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
            .assertSuccess();
    }

    @Test
    void testExecuteMergedRootRouteCommands() {
        platform.execute("first test")
            .assertMissingPermission("test.permission", "test.permission.execute");

        platform.execute("third")
            .assertMissingPermission("test.permission");
    }

}