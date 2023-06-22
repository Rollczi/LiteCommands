package dev.rollczi.litecommands.annotations.async;

import dev.rollczi.litecommands.annotations.LiteConfig;
import dev.rollczi.litecommands.annotations.LiteConfigurator;
import dev.rollczi.litecommands.annotations.LiteTestSpec;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.argument.ArgumentResolverInfo;
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
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

class AsyncCommandTest extends LiteTestSpec {

    @LiteConfigurator
    static LiteConfig config() {
        return builder -> builder
//            .scheduler(new SchedulerExecutorPoolImpl("test", 1))
            .context(Date.class, invocation -> ContextResult.ok(() -> {
                try {
                    Thread.sleep(800);
                }
                catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                return new Date();
            }))
            .argument(SomeClass.class, new ThrowingArgumentResolver());
    }

    static class SomeClass {}

    @ArgumentResolverInfo(name = "throwing")
    static class ThrowingArgumentResolver extends ArgumentResolver<TestSender, SomeClass> {

        @Override
        protected ParseResult<SomeClass> parse(Invocation<TestSender> invocation, Argument<SomeClass> context, String argument) {
            throw new IllegalArgumentException();
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
            return Thread.currentThread().getName();
        }

        @Async
        @Execute(name = "async-args-and-method")
        public String testAsyncArgs2(@Context Date date, @Arg String first, @Async @Arg String second) {
            return Thread.currentThread().getName();
        }

        @Execute(name = "async-args-and-method-throwing")
        public String testAsyncArgs3(@Async @Arg SomeClass someClass) {
            return Thread.currentThread().getName();
        }

    }

    @Test
    void testSync() {
        for (int i = 0; i < 1_000_000; i++) {

            platform.execute("test sync")/*
                .assertSuccess("scheduler-test-main")*/;
        }
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
            .atLeast(300, TimeUnit.MILLISECONDS)
            .until(() -> result.isDone());

        result.join()
            .assertSuccess("scheduler-test-main");
    }

    @Test
    void testAsyncArgsAndMethod() {
        CompletableFuture<AssertExecute> result = platform.executeAsync("test async-args-and-method first second");

        await()
            .atLeast(300, TimeUnit.MILLISECONDS)
            .until(() -> result.isDone());

        result.join()
            .assertSuccess("scheduler-test-async-0");
    }

    @Test
    void testAsyncArgsAndMethodThrowing() {
        platform.execute("test async-args-and-method-throwing throwing some-arg")
            .assertThrows(IllegalArgumentException.class);
    }

}
