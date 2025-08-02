package dev.rollczi.litecommands.minestom;

import dev.rollczi.litecommands.scheduler.Scheduler;
import dev.rollczi.litecommands.scheduler.SchedulerType;
import dev.rollczi.litecommands.shared.ThrowingSupplier;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.SchedulerManager;
import net.minestom.server.timer.Task;

public class MinestomScheduler implements Scheduler {

    private final SchedulerManager scheduler;
    private final Scheduler asyncScheduler;

    public MinestomScheduler(SchedulerManager scheduler, Scheduler asyncScheduler) {
        this.scheduler = scheduler;
        this.asyncScheduler = asyncScheduler;
    }

    @Override
    public <T> CompletableFuture<T> supplyLater(SchedulerType origin, Duration delay, ThrowingSupplier<T, Throwable> supplier) {
        SchedulerType type = origin.resolve(SchedulerType.MAIN, SchedulerType.ASYNCHRONOUS)
            .orElseThrow(() -> new IllegalStateException("Cannot resolve the thread type"));
        CompletableFuture<T> future = new CompletableFuture<>();

        if (type == SchedulerType.MAIN || type == MinestomSchedulerType.TICK_START || type == MinestomSchedulerType.TICK_END) {
            Task.Builder built = scheduler.buildTask(() -> tryRun(supplier, future));

            if (!delay.isZero()) {
                built.delay(delay);
            }

            if (type == MinestomSchedulerType.TICK_START) {
                built.executionType(ExecutionType.TICK_START);
            }

            if (type == MinestomSchedulerType.TICK_END) {
                built.executionType(ExecutionType.TICK_END);
            }

            built.schedule();
            return future;
        }

        return asyncScheduler.supplyLater(SchedulerType.ASYNCHRONOUS, delay, supplier);
    }

    @Override
    public void shutdown() {
        asyncScheduler.shutdown();
    }

    private <T> void tryRun(ThrowingSupplier<T, Throwable> supplier, CompletableFuture<T> future) {
        try {
            future.complete(supplier.get());
        } catch (Throwable throwable) {
            future.completeExceptionally(throwable);
        }

    }

}
