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

}
