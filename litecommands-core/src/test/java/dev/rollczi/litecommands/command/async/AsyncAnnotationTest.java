package dev.rollczi.litecommands.command.async;

import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import dev.rollczi.litecommands.handle.LiteException;
import dev.rollczi.litecommands.test.AssertResult;
import dev.rollczi.litecommands.test.TestFactory;
import dev.rollczi.litecommands.test.TestPlatform;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class AsyncAnnotationTest {

    TestPlatform platform = TestFactory.create(builder -> builder
        .command(Command.class)
        .resultHandler(LiteException.class, (testHandle, invocation, value) -> testHandle.message(value.getResult().toString()))
        .resultHandler(Throwable.class, (testHandle, invocation, value) -> testHandle.message(value.getMessage()))
    );

    @Route(name = "command")
    static class Command {

        @Async
        @Execute(route = "async")
        String execute() {
            return "async";
        }

        @Async
        @Execute(route = "async-exception")
        String executeException() {
            throw new LiteException("async-exception");
        }

        @Async
        @Execute(route = "async-throwable")
        String executeThrowable() throws Throwable {
            throw new Throwable("async-throwable");
        }

    }

    @Test
    void test() {
        this.platform.execute("command", "async")
            .assertSuccess()
            .assertResultIs(CompletableFuture.class);
    }

    @Test
    @DisplayName("should handle thrown exception in async method")
    void testThrowException() {
        AssertResult assertResult = this.platform.execute("command", "async-exception");
        Object result = assertResult
            .assertSuccess()
            .assertResultIs(CompletableFuture.class)
            .join();

        LiteException liteException = assertInstanceOf(LiteException.class, result);

        assertEquals("async-exception", liteException.getResult());
        assertResult.assertMessage("async-exception");
    }

    @Test
    @DisplayName("should handle thrown throwable in async method")
    void testThrowThrowable() {
        AssertResult assertResult = this.platform.execute("command", "async-throwable");
        Object result = assertResult
            .assertSuccess()
            .assertResultIs(CompletableFuture.class)
            .join();

        Throwable throwable = assertInstanceOf(Throwable.class, result);

        assertEquals("async-throwable", throwable.getMessage());
        assertResult.assertMessage("async-throwable");
    }

}
