package dev.rollczi.litecommands.modern;

import dev.rollczi.litecommands.modern.annotation.argument.Arg;
import dev.rollczi.litecommands.modern.annotation.contextual.Context;
import dev.rollczi.litecommands.modern.annotation.execute.Execute;
import dev.rollczi.litecommands.modern.annotation.route.Route;
import dev.rollczi.litecommands.modern.env.FakeSender;
import dev.rollczi.litecommands.modern.test.LiteTest;
import dev.rollczi.litecommands.modern.test.LiteTestConfig;
import dev.rollczi.litecommands.modern.test.LiteTestSpec;
import dev.rollczi.litecommands.modern.test.TestConfigurator;
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

        @Execute(name = "opt")
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
