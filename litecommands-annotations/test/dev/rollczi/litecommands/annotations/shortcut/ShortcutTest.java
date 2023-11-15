package dev.rollczi.litecommands.annotations.shortcut;

import dev.rollczi.litecommands.annotations.LiteTestSpec;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.junit.jupiter.api.Test;
import panda.std.Option;

import static dev.rollczi.litecommands.unit.TestPlatformSender.permitted;

class ShortcutTest extends LiteTestSpec {

    @Command(name = "base")
    @Permission("base.permission")
    static class TestCommand {

        @Execute(name = "executor")
        @Shortcut("short")
        @Permission("executor.permission")
        String executeOpt(@Arg String text, @Arg Option<String> test) {
            return text + ":" + test.orElseGet("none");
        }

    }

    @Command(name = "multi base")
    @Permission("base.multi")
    static class TestMultiCommand {

        @Execute(name = "executor")
        @Shortcut("short-multi")
        @Permission("executor.multi")
        String executeOpt(@Arg String text, @Arg Option<String> test) {
            return text + ":" + test.orElseGet("none");
        }

    }

    @Test
    void testExecuteNormalCommand() {
        platform.execute(permitted("base.permission", "executor.permission"), "base executor key value")
            .assertSuccess("key:value");
    }

    @Test
    void testExecuteShortCommand() {
        platform.execute(permitted("base.permission", "executor.permission"), "short key value")
            .assertSuccess("key:value");
    }

    @Test
    void testExecuteShortCommandWithoutPermission() {
        platform.execute("short key value")
            .assertMissingPermission("executor.permission", "base.permission");
    }

    @Test
    void testExecuteShortMultiCommand() {
        platform.execute(permitted("base.multi", "executor.multi"), "short-multi key value")
            .assertSuccess("key:value");
    }

    @Test
    void testExecuteShortMultiCommandWithoutPermission() {
        platform.execute("short-multi key value")
            .assertMissingPermission("executor.multi", "base.multi");
    }

}
