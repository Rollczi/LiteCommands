package dev.rollczi.litecommands.annotation;

import dev.rollczi.litecommands.annotation.argument.Arg;
import dev.rollczi.litecommands.annotation.context.Context;
import dev.rollczi.litecommands.annotation.execute.Execute;
import dev.rollczi.litecommands.annotation.route.Route;
import dev.rollczi.litecommands.test.FakeSender;
import org.junit.jupiter.api.Test;
import panda.std.Option;

@LiteTest
class ExampleUnitTest extends LiteTestSpec {

    @LiteTestConfig
    static TestConfigurator config() {
        return builder ->
            builder.editor("route", context -> context.aliases("r"));
    }

    @Route(name = "route")
    static class Command {

        @Execute
        String execute(
            @Context FakeSender sender,
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
