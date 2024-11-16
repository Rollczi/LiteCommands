package dev.rollczi.litecommands.scheduler;

import dev.rollczi.litecommands.shared.ThrowingSupplier;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public abstract class AbstractMainThreadBasedScheduler implements Scheduler {

    @Override
    public final  <T> CompletableFuture<T> supplyLater(SchedulerPoll type, Duration delay, ThrowingSupplier<T, Throwable> supplier) {
        SchedulerPoll resolved = type.resolve(SchedulerPoll.MAIN, SchedulerPoll.ASYNCHRONOUS);
        CompletableFuture<T> future = new CompletableFuture<>();

        if (resolved.equals(SchedulerPoll.MAIN)) {
            runSynchronous(() -> tryRun(type, future, supplier), delay);
            return future;
        }

        if (resolved.equals(SchedulerPoll.ASYNCHRONOUS)) {
            runAsynchronous(() -> tryRun(type, future, supplier), delay);
            return future;
        }

        throw new IllegalArgumentException("Unknown scheduler poll type: " + type);
    }

    abstract protected void runSynchronous(Runnable task, Duration delay);

    protected abstract void runAsynchronous(Runnable task, Duration delay);

    private <T> void tryRun(SchedulerPoll type, CompletableFuture<T> future, ThrowingSupplier<T, Throwable> supplier) {
        try {
            future.complete(supplier.get());
        }
        catch (Throwable throwable) {
            future.completeExceptionally(throwable);

            if (type.isLogging()) {
                this.log(throwable);
            }
        }
    }

    protected void log(Throwable throwable) {}

}
