package dev.rollczi.litecommands.minestom;

import dev.rollczi.litecommands.scheduler.Scheduler;
import dev.rollczi.litecommands.scheduler.SchedulerPoll;
import dev.rollczi.litecommands.shared.ThrowingSupplier;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.SchedulerManager;
import net.minestom.server.timer.Task;

public class MinestomScheduler implements Scheduler {

    private final SchedulerManager scheduler;

    public MinestomScheduler(SchedulerManager scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public <T> CompletableFuture<T> supplyLater(SchedulerPoll type, Duration delay, ThrowingSupplier<T, Throwable> supplier) {
        SchedulerPoll poll = type.resolve(SchedulerPoll.MAIN, SchedulerPoll.ASYNCHRONOUS);
        CompletableFuture<T> future = new CompletableFuture<>();

        Task.Builder built = scheduler.buildTask(() -> tryRun(supplier, future))
            .executionType(poll == SchedulerPoll.MAIN ? ExecutionType.SYNC : ExecutionType.ASYNC);

        if (!delay.isZero()) {
            built.delay(delay);
        }

        built.schedule();
        return future;
    }

    @Override
    public void shutdown() {
    }

    private <T> CompletableFuture<T> tryRun(ThrowingSupplier<T, Throwable> supplier, CompletableFuture<T> future) {
        try {
            future.complete(supplier.get());
        } catch (Throwable throwable) {
            future.completeExceptionally(throwable);
        }

        return future;
    }

}
