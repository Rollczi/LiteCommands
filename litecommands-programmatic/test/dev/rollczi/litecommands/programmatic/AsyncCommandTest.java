package dev.rollczi.litecommands.programmatic;

import dev.rollczi.litecommands.unit.annotations.LiteTestSpec;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.context.ContextResult;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.scheduler.SchedulerExecutorPoolImpl;
import dev.rollczi.litecommands.unit.TestSender;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static dev.rollczi.litecommands.programmatic.LiteProgrammatic.*;

class AsyncCommandTest extends LiteTestSpec {

    static LiteTestConfig config = builder -> builder
        .scheduler(new SchedulerExecutorPoolImpl("test"))
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
            .assertSuccess("scheduler-test-main");
    }

    @Test
    void testAsync() {
        platform.execute("test async")
            .assertSuccess("scheduler-test-async-0");
    }

    @Test
    void testAsyncArgs() {
        platform.execute("test async-args first second")
            .assertSuccess("scheduler-test-main args [first=scheduler-test-main, second=scheduler-test-async-0]");
    }

    @Test
    void testAsyncArgsAndMethod() {
        platform.execute("test async-args-and-method first second")
            .assertSuccess("scheduler-test-async-0 args [first=scheduler-test-async-0, second=scheduler-test-main]");
    }

    @Test
    void testAsyncArgsAndMethodThrowing() {
        platform.execute("test async-args-and-method-throwing throwing some-arg")
            .assertThrows(IllegalArgumentException.class);
    }

}
