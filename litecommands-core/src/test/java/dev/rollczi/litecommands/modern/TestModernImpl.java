package dev.rollczi.litecommands.modern;

import dev.rollczi.litecommands.modern.annotation.argument.Arg;
import dev.rollczi.litecommands.modern.annotation.contextual.Context;
import dev.rollczi.litecommands.modern.annotation.execute.Execute;
import dev.rollczi.litecommands.modern.annotation.route.Route;
import dev.rollczi.litecommands.modern.command.CommandExecuteResult;
import dev.rollczi.litecommands.modern.command.CommandManager;
import dev.rollczi.litecommands.modern.env.FakePlatform;
import dev.rollczi.litecommands.modern.env.FakeSender;
import org.junit.jupiter.api.Test;
import panda.std.Option;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestModernImpl {

    FakePlatform fakePlatform = new FakePlatform();
    LiteCommands<FakeSender> register = LiteCommandsFactory.annotation(FakeSender.class)
        .command(new Command())
        .platform(fakePlatform)
        .resultHandler(String.class, (invocation, result) -> {})
        .register();

    @Route(name = "route")
    public static class Command {

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
    void test() {
        CommandExecuteResult executeResult = fakePlatform.execute("route", "key", "value").getCommandExecuteResult();
        assertEquals("key:value", executeResult.getResult().get());
    }

    @Test
    void testOpt() {
        CommandExecuteResult executeResult = fakePlatform.execute("route", "opt", "key", "value").getCommandExecuteResult();
        assertEquals("key:value", executeResult.getResult().get());
    }

//    @Test
    void executeForProfiler() {
        long start = System.currentTimeMillis();

        for (int i = 0; i < 1_000_000; i++) {
            fakePlatform.execute("route", "key", "value");
            fakePlatform.execute("route", "opt", "key", "value");
        }

        long result = System.currentTimeMillis() - start;
        System.out.println(result);
    }

}
