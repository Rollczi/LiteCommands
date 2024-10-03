package dev.rollczi.litecommands.annotations.async;

import dev.rollczi.litecommands.unit.annotations.LiteTestSpec;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.context.ContextResult;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.scheduler.SchedulerExecutorPoolImpl;
import dev.rollczi.litecommands.unit.AssertExecute;
import dev.rollczi.litecommands.unit.TestSender;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.Test;

public class ParallelAsyncCommandTest extends LiteTestSpec {

    private static final int DELAY = 100;

    static LiteTestConfig config = builder -> builder
        .scheduler(new SchedulerExecutorPoolImpl("test", 50))
        .context(Date.class, invocation -> ContextResult.ok(() -> {
            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            return new Date();
        }))
        .argumentParser(String.class, new StringArgumentResolver());

    static class StringArgumentResolver extends ArgumentResolver<TestSender, String> {
        @Override
        protected ParseResult<String> parse(Invocation<TestSender> invocation, Argument<String> context, String argument) {
            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            return ParseResult.success(argument);
        }
    }

    @Command(name = "async")
    static class TestCommand {

        @Async
        @Execute
        public String testAsyncArgs2(@Async @Context Date date, @Async @Arg String first, @Async @Arg String second) {
            return first + " " + second;
        }

    }

    @Test
    void testAsyncArgsAndMethod() {
        List<CompletableFuture<AssertExecute>> results = new ArrayList<>();

        for (int i = 0; i < 500; i++) {
            results.add(platform.executeAsync("async first second"));
        }

        for (CompletableFuture<AssertExecute> result : results) {
            result.join()
                .assertSuccess("first second");
        }
    }


}
