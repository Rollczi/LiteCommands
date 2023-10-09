package dev.rollczi.litecommands.annotations;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.bind.Bind;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.scope.Scope;
import dev.rollczi.litecommands.unit.TestSender;
import org.junit.jupiter.api.Test;
import panda.std.Option;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class ExampleUnitTest extends LiteTestSpec {

    static LiteConfig config = builder -> builder
        .editor(Scope.command("route"), context -> context.aliases("r"))
        .bind(ExecutorService.class, () -> Executors.newFixedThreadPool(4));

    @Command(name = "route")
    static class TestCommand {

        @Execute
        String execute(
            @Context TestSender sender,
            @Arg String text,
            @Arg String test
        ) {
            return text + ":" + test;
        }

        @Execute(name = "opt")
        String executeOpt(@Arg String text, @Arg Option<String> test) {
            return text + ":" + test.orElseGet("none");
        }

        @Execute(name = "closeExecutor")
        String closeExecutor(@Bind ExecutorService executor) {
            executor.shutdown();
            return "closed";
        }

    }

    @Test
    void testExecuteNormalCommand() {
        platform.execute("route key value")
            .assertSuccess("key:value");
    }

    @Test
    void testExecuteCommandWithOption() {
        platform.execute("route opt key value")
            .assertSuccess("key:value");
    }

    @Test
    void testDynamicAlias() {
        platform.execute("r key value")
            .assertSuccess("key:value");
    }

    @Test
    void testCloseExecutor() {
        platform.execute("route closeExecutor")
            .assertSuccess("closed");
    }

}
