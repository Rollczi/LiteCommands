package dev.rollczi.litecommands.annotations.permission;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.command.RootCommand;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.priority.Priority;
import dev.rollczi.litecommands.annotations.priority.PriorityValue;
import dev.rollczi.litecommands.unit.annotations.LiteTestSpec;
import java.time.Duration;
import org.junit.jupiter.api.Test;

class MergeCommandPermissionHandleTest extends LiteTestSpec {

    @Command(name = "test")
    @Permission("test.classOne.permission")
    static class TestCommandOne {

        @Execute(name = "reload")
        void executeReload() {}

        @Execute(name = "sub")
        @Permission("test.method.permission.string")
        void executeSub(@Arg String text) {}

    }

    @Test
    void testReloadCommandRequiresOnlyClassPermission() {
        platform.execute("test reload")
            .assertMissingPermission("test.classOne.permission");
    }


    @Test
    void testSubCommandWithStringArgRequiresClassAndMethodPermissions() {
        platform.execute("test sub arg")
            .assertMissingPermission("test.method.permission.string", "test.classOne.permission");
    }


    @Command(name = "test")
    @Permission("test.classTwo.permission")
    static class TestCommandTwo {

        @Execute(name = "sub")
        @Permission("test.method.permission")
        void executeSub() {}

    }

    @Test
    void testSubCommandNoArgsRequiresClassAndMethodPermissions() {
        platform.execute("test sub")
            .assertMissingPermission("test.method.permission", "test.classTwo.permission");
    }

    @Command(name = "test")
    static class TestCommandThree {

        @Execute
        @Permission("test.root.three")
        void executeRoot() {}

        @Execute(name = "multi")
        @Permission("test.multi.three")
        void executeMulti(@Arg String arg1, @Arg int arg2, @Arg boolean arg3) {}

    }

    @Test
    void testRootExecutorNoArgs() {
        platform.execute("test")
            .assertMissingPermission("test.root.three");
    }

    @Test
    void testMultiArgCommandRequiresOnlyMethodPermission() {
        platform.execute("test multi hello 123 true")
            .assertMissingPermission("test.multi.three");
    }

    @Command(name = "test")
    @Permission("test.class.four")
    static class TestCommandFour {

        @Execute(name = "sub")
        void executeSubWithTwoArgs(@Arg String arg1, @Arg String arg2) {}

    }

    @Test
    void testSubCommandWithTwoStringArgsRequiresOnlyClassPermissionFromItsClass() {
        platform.execute("test sub arg1 arg2")
            .assertMissingPermission("test.class.four");
    }

    @RootCommand
    static class RootCommandImpl {

        @Execute(name = "test")
        @Priority(PriorityValue.HIGH)
        @Permission("root.amount")
        void executeAmount(@Arg int amount) {}

    }

    @Test
    void testRootCommandWithIntArg() {
        platform.execute("test 123")
            .assertMissingPermission("root.amount");
    }

    @RootCommand
    @Permission("root.two.class")
    static class RootCommandTwo {
        @Execute(name = "test")
        void executeWithString(@Arg String text) {}
    }

    @Test
    void testRootCommandWithStringArg() {
        platform.execute("test hello")
            .assertMissingPermission("root.two.class");
    }

    @RootCommand
    static class RootCommandDuration {
        @Execute(name = "test")
        @Priority(PriorityValue.HIGH)
        @Permission("root.duration")
        void executeOtherCmd(@Arg Duration text) {}
    }

    @Test
    void testDurationRootCommand() {
        platform.execute("test 1d")
            .assertMissingPermission("root.duration");
    }

    @RootCommand
    @Permission("root.four.class")
    static class RootCommandFour {
        @Execute(name = "test")
        @Permission("root.four.method")
        void executeWithTwoArgs(@Arg String text, @Arg int number) {}
    }

    @Test
    void testRootCommandWithTwoArgsAndCombinedPermissions() {
        platform.execute("test hello 123")
            .assertMissingPermission("root.four.class", "root.four.method");
    }

}