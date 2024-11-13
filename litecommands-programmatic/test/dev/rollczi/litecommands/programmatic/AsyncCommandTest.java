package dev.rollczi.litecommands.programmatic;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.context.ContextResult;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.scheduler.SchedulerExecutorPoolBuilder;
import dev.rollczi.litecommands.unit.TestSender;
import dev.rollczi.litecommands.unit.annotations.LiteTestSpec;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static dev.rollczi.litecommands.programmatic.LiteProgrammatic.async;

class AsyncCommandTest extends LiteTestSpec {

    private static final String mainThreadName = "main_thread";
    private static final String asyncThreadName = "async_thread";

    static LiteTestConfig config = builder -> builder
        .scheduler(
            new SchedulerExecutorPoolBuilder()
                .setMainExecutorThreadName(mainThreadName)
                .setAsyncExecutorThreadName(asyncThreadName)
                .build()
        )
        .context(Date.class, invocation -> ContextResult.ok(() -> new Date()))
        .argumentParser(String.class, new StringArgumentResolver())
        .argument(SomeClass.class, new ThrowingArgumentResolver())
        .commands(
            new LiteCommand<TestSender>("test")
                .subcommand("sync", syncCommand -> syncCommand
                    .executeReturn(context -> {
                        TestSender sender = context.invocation().sender();
                        return Thread.currentThread().getName();
                    })
                )
                .subcommand("async", asyncCommand -> asyncCommand
                    .async()
                    .executeReturn(context -> {
                        TestSender sender = context.invocation().sender();
                        return Thread.currentThread().getName();
                    })
                )
                .subcommand("async-args", asyncArgsCommand -> asyncArgsCommand
                    .context("date", Date.class)
                    .argument("first", String.class)
                    .argument(async(Argument.of("second", String.class)))
                    .executeReturn(context -> {
                        Date date = context.context("date", Date.class);
                        String first = context.argument("first", String.class);
                        String second = context.argument("second", String.class);

                        return Thread.currentThread().getName() + " args [first=" + first + ", second=" + second + "]";
                    })
                )
                .subcommand("async-args-and-method", asyncArgsMethodCommand -> asyncArgsMethodCommand
                    .context("date", Date.class)
                    .argument(async(Argument.of("first", String.class)))
                    .argument("second", String.class)
                    .async()
                    .executeReturn(context -> {
                        Date date = context.context("date", Date.class);
                        String first = context.argument("first", String.class);
                        String second = context.argument("second", String.class);

                        return Thread.currentThread().getName() + " args [first=" + first + ", second=" + second + "]";
                    })
                )
                .subcommand("async-args-and-method-throwing", asyncArgsThrowingCommand -> asyncArgsThrowingCommand
                    .argument(async(Argument.of("someClass", SomeClass.class)))
                    .async()
                    .executeReturn(context -> {
                        SomeClass someClass = context.argument("someClass", SomeClass.class);
                        return Thread.currentThread().getName();
                    })
                )
        );

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

    @Test
    void testSync() {
        platform.execute("test sync")
            .assertSuccess(mainThreadName);
    }

    @Test
    void testAsync() {
        platform.execute("test async")
            .assertSuccess(asyncThreadName);
    }

    @Test
    void testAsyncArgs() {
        platform.execute("test async-args first second")
            .assertSuccess(mainThreadName + " args [first=" + mainThreadName + ", second=" + asyncThreadName + "]");
    }

    @Test
    void testAsyncArgsAndMethod() {
        platform.execute("test async-args-and-method first second")
            .assertSuccess(asyncThreadName + " args [first=" + asyncThreadName + ", second=" + mainThreadName + "]");
    }

    @Test
    void testAsyncArgsAndMethodThrowing() {
        platform.execute("test async-args-and-method-throwing throwing some-arg")
            .assertThrows(IllegalArgumentException.class);
    }

}
