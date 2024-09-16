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
    private final Scheduler asyncScheduler;

    public MinestomScheduler(SchedulerManager scheduler, Scheduler asyncScheduler) {
        this.scheduler = scheduler;
        this.asyncScheduler = asyncScheduler;
    }

    @Override
    public <T> CompletableFuture<T> supplyLater(SchedulerPoll type, Duration delay, ThrowingSupplier<T, Throwable> supplier) {
        SchedulerPoll poll = type.resolve(SchedulerPoll.MAIN, SchedulerPoll.ASYNCHRONOUS, MinestomSchedulerPoll.TICK_START, MinestomSchedulerPoll.TICK_END);
        CompletableFuture<T> future = new CompletableFuture<>();

        if (poll == SchedulerPoll.MAIN || poll == MinestomSchedulerPoll.TICK_START || poll == MinestomSchedulerPoll.TICK_END) {
            Task.Builder built = scheduler.buildTask(() -> tryRun(supplier, future));

            if (!delay.isZero()) {
                built.delay(delay);
            }

            if (poll == MinestomSchedulerPoll.TICK_START) {
                built.executionType(ExecutionType.TICK_START);
            }

            if (poll == MinestomSchedulerPoll.TICK_END) {
                built.executionType(ExecutionType.TICK_END);
            }

            built.schedule();
            return future;
        }

        return asyncScheduler.supplyLater(SchedulerPoll.ASYNCHRONOUS, delay, supplier);
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
