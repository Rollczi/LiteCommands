package dev.rollczi.litecommands.annotations.root;

import dev.rollczi.litecommands.unit.annotations.LiteTestSpec;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.RootCommand;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class RootCommandTest extends LiteTestSpec {

    @RootCommand
    static class Command {
        @Execute(name = "first")
        public void test() {}

        @Execute(name = "first")
        @Permission("test.permission.execute.two")
        public void test(@Arg String one, @Arg String two) {}

        @Execute(name = "second")
        public void test2() {}
    }

    @RootCommand
    @Permission("test.permission")
    static class Command2 {
        @Execute(name = "first")
        @Permission("test.permission.execute")
        public void test(@Arg String test) {}

        @Execute(name = "third")
        public void test2() {}
    }

    @Test
    void verifyStructure() {
        assertThat(platform.findCommand("first").getExecutors())
            .hasSize(3);
    }

    @Test
    void testExecuteRootRouteCommands() {
        platform.execute("first")
            .assertSuccess();

        platform.execute("second")
            .assertSuccess();
    }

    @Test
    void testExecuteMergedRootRouteCommands() {
        platform.execute("first test")
            .assertMissingPermission("test.permission", "test.permission.execute");

        platform.execute("first one two")
            .assertMissingPermission("test.permission.execute.two");

        platform.execute("third")
            .assertMissingPermission("test.permission");
    }

}