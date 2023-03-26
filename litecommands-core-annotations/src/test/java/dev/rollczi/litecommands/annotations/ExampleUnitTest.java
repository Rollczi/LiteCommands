package dev.rollczi.litecommands.annotations;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.route.Route;
import dev.rollczi.litecommands.unit.TestSender;
import org.junit.jupiter.api.Test;
import panda.std.Option;

@LiteTest
class ExampleUnitTest extends LiteTestSpec {

    @LiteTestConfig
    static LiteTestConfigurator config() {
        return builder ->
            builder.editor("route", context -> context.aliases("r"));
    }

    @Route(name = "route")
    static class Command {

        @Execute
        String execute(
            @Context TestSender sender,
            @Arg String text,
            @Arg String test
        ) {
            return text + ":" + test;
        }

        @Execute(route = "opt")
        String executeOpt(@Arg String text, @Arg Option<String> test) {
            return text + ":" + test.orElseGet("none");
        }

    }

    @Test
    void testExecuteNormalCommand() {
        platform.execute("route key value")
            .assertSuccessful("key:value");
    }

    @Test
    void testExecuteCommandWithOption() {
        platform.execute("route opt key value")
            .assertSuccessful("key:value");
    }

    @Test
    void testDynamicAlias() {
        platform.execute("r key value")
            .assertSuccessful("key:value");
    }

}
