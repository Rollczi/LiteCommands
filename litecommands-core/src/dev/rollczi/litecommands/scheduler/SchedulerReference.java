package dev.rollczi.litecommands.scheduler;

import dev.rollczi.litecommands.shared.Preconditions;
import dev.rollczi.litecommands.shared.ThrowingSupplier;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class SchedulerReference implements Scheduler {

    private Scheduler scheduler;

    public SchedulerReference(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void setScheduler(Scheduler scheduler) {
        Preconditions.notNull(scheduler, "Scheduler cannot be null");
        this.scheduler.shutdown();
        this.scheduler = scheduler;
    }

    @Override
    public <T> CompletableFuture<T> supplyLater(SchedulerPoll type, Duration delay, ThrowingSupplier<T, Throwable> supplier) {
        return scheduler.supplyLater(type, delay, supplier);
    }

    @Override
    public void shutdown() {
        scheduler.shutdown();
    }

}
