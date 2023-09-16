package dev.rollczi.litecommands.annotations;

import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.command.Command;
import dev.rollczi.litecommands.context.Context;
import dev.rollczi.litecommands.command.executor.Execute;
import dev.rollczi.litecommands.scope.Scope;
import dev.rollczi.litecommands.unit.TestSender;
import org.junit.jupiter.api.Test;
import panda.std.Option;

class ExampleUnitTest extends LiteTestSpec {

    static LiteConfig config = builder ->
            builder.editor(Scope.command("route"), context -> context.aliases("r"));

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

}
