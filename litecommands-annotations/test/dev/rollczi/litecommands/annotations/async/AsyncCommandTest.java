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
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

class AsyncCommandTest extends LiteTestSpec {

    private static final int DELAY = 500;
    private static final int MARGIN = 200;

    static LiteTestConfig config = builder -> builder
        .scheduler(new SchedulerExecutorPoolImpl("test"))
        .context(Date.class, invocation -> ContextResult.ok(() -> {
            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            return new Date();
        }))
        .argumentParser(String.class, new StringArgumentResolver())
        .argument(SomeClass.class, new ThrowingArgumentResolver());

    static class SomeClass {
    }

    static class ThrowingArgumentResolver extends ArgumentResolver<TestSender, SomeClass> {
        @Override
        protected ParseResult<SomeClass> parse(Invocation<TestSender> invocation, Argument<SomeClass> context, String argument) {
            throw new IllegalArgumentException();
        }
    }

    static class StringArgumentResolver extends ArgumentResolver<TestSender, String> {
        @Override
        protected ParseResult<String> parse(Invocation<TestSender> invocation, Argument<String> context, String argument) {
            return ParseResult.success(Thread.currentThread().getName());
        }
    }

    @Command(name = "test")
    static class TestCommand {

        @Execute(name = "sync")
        public String test() {
            return Thread.currentThread().getName();
        }

        @Async
        @Execute(name = "async")
        public String testAsync() {
            return Thread.currentThread().getName();
        }

        @Execute(name = "async-args")
        public String testAsyncArgs(@Context Date date, @Arg String first, @Async @Arg String second) {
            return Thread.currentThread().getName() + " args [first=" + first + ", second=" + second + "]";
        }

        @Async
        @Execute(name = "async-args-and-method")
        public String testAsyncArgs2(@Context Date date,  @Async @Arg String first, @Arg String second) {
            return Thread.currentThread().getName() + " args [first=" + first + ", second=" + second + "]";
        }

        @Execute(name = "async-args-and-method-throwing")
        public String testAsyncArgs3(@Async @Arg SomeClass someClass) {
            return Thread.currentThread().getName();
        }

    }

    @Test
    void testSync() {
        platform.execute("test sync")
            .assertSuccess("scheduler-test-main");
    }

    @Test
    void testAsync() {
        platform.execute("test async")
            .assertSuccess("scheduler-test-async-0");
    }

    @Test
    void testAsyncArgs() {
        CompletableFuture<AssertExecute> result = platform.executeAsync("test async-args first second");

        await()
            .atMost(DELAY + MARGIN, TimeUnit.MILLISECONDS)
            .until(() -> result.isDone());

        result.join()
            .assertSuccess("scheduler-test-main args [first=scheduler-test-main, second=scheduler-test-async-0]");
    }

    @Test
    void testAsyncArgsAndMethod() {
        CompletableFuture<AssertExecute> result = platform.executeAsync("test async-args-and-method first second");

        await()
            .atMost(DELAY + MARGIN, TimeUnit.MILLISECONDS)
            .until(() -> result.isDone());

        result.join()
            .assertSuccess("scheduler-test-async-0 args [first=scheduler-test-async-0, second=scheduler-test-main]");
    }

    @Test
    void testAsyncArgsAndMethodThrowing() {
        platform.execute("test async-args-and-method-throwing throwing some-arg")
            .assertThrows(IllegalArgumentException.class);
    }

}
