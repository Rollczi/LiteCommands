package dev.rollczi.litecommands.scheduler;

import dev.rollczi.litecommands.shared.ThrowingSupplier;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public abstract class AbstractMainThreadBasedScheduler implements Scheduler {

    @Override
    public final <T> CompletableFuture<T> supplyLater(SchedulerPoll type, Duration delay, ThrowingSupplier<T, Throwable> supplier) {
        SchedulerPoll resolved = type.resolve(SchedulerPoll.MAIN, SchedulerPoll.ASYNCHRONOUS);
        CompletableFuture<T> future = new CompletableFuture<>();
        Runnable task = () -> tryRun(type, future, supplier);

        if (resolved.equals(SchedulerPoll.MAIN)) {
            runSynchronous(task, delay);
            return future;
        }

        if (resolved.equals(SchedulerPoll.ASYNCHRONOUS)) {
            runAsynchronous(task, delay);
            return future;
        }

        boolean isResolved = runUnknown(type, delay, task);
        if (isResolved) {
            return future;
        }

        throw new IllegalArgumentException("Unknown scheduler poll type: " + type);
    }

    /**
     * Run task with an unknown scheduler poll type
     * @return true if a task was run, false otherwise
     */
    protected boolean runUnknown(SchedulerPoll type, Duration delay, Runnable task) {
        return false;
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
