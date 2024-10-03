package dev.rollczi.litecommands.argument.resolver.completeable;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.reflect.type.TypeToken;
import dev.rollczi.litecommands.scheduler.SchedulerExecutorPoolImpl;
import dev.rollczi.litecommands.unit.annotations.LiteTestSpec;
import dev.rollczi.litecommands.unit.blocking.Blocking;
import dev.rollczi.litecommands.unit.blocking.BlockingArgument;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import static org.awaitility.Awaitility.await;
import org.junit.jupiter.api.Test;

class CompletableFutureArgumentTest extends LiteTestSpec {

    static LiteTestConfig config = builder -> builder
        .scheduler(new SchedulerExecutorPoolImpl("litecommands-test"));

    public static final Class<CompletableFuture<String>> FURUTRE_TYPE = new TypeToken<CompletableFuture<String>>() {}.getRawType();

    @Command(name = "async")
    static class TestCommand {

        @Execute
        public CompletableFuture<String> testAsync(
            @Arg @Blocking(200) CompletableFuture<BlockingArgument> futureA,
            @Arg @Blocking(300) CompletableFuture<BlockingArgument> futureB
        ) {
            return futureA.thenCombine(futureB, (a, b) -> a.getInput() + b.getInput());
        }

    }

    @Test
    void test() {
        for (int i = 0; i < 1_000; i++) {
            CompletableFuture<String> completableFuture = platform.executeAsync("async a b")
                .thenCompose(assertExecute -> assertExecute.assertSuccessAs(FURUTRE_TYPE));

            long start = System.nanoTime();
            await()
                .until(() -> completableFuture.isDone());
            long time = System.nanoTime() - start;
            System.out.println("Time: " + TimeUnit.NANOSECONDS.toMillis(time) + "ms");
        }

    }

}