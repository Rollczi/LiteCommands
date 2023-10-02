package dev.rollczi.litecommands.scheduler;

import panda.std.function.ThrowingRunnable;
import panda.std.function.ThrowingSupplier;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 * Scheduler is used to run the commands, tasks, etc.
 */
public interface Scheduler {

    /**
     * Runs the supplier in the specified thread
     *
     * @param type the thread type
     * @param runnable the runnable to run
     * @return the supplier result
     */
    default CompletableFuture<Void> run(SchedulerPoll type, ThrowingRunnable<Throwable> runnable) {
        return supply(type, () -> {
            runnable.run();
            return null;
        });
    }

    /**
     * Runs the supplier in the specified thread
     * after the specified delay
     * @param type the thread type
     * @param delay the delay
     * @param runnable the runnable to run
     * @return the supplier result
     */
    default CompletableFuture<Void> runLater(SchedulerPoll type, Duration delay, ThrowingRunnable<Throwable> runnable) {
        return supplyLater(type, delay, () -> {
            runnable.run();
            return null;
        });
    }

    /**
     * Runs the supplier in the specified thread
     *
     * @param type the thread type
     * @param supplier the supplier to run
     * @param <T> the supplier return type
     * @return the supplier result
     */
    default <T> CompletableFuture<T> supply(SchedulerPoll type, ThrowingSupplier<T, Throwable> supplier) {
        return supplyLater(type, Duration.ZERO, supplier);
    }

    /**
     * Runs the supplier in the specified thread
     * after the specified delay
     * @param type the thread type
     * @param delay the delay
     * @param supplier the supplier to run
     * @param <T> the supplier return type
     * @return the supplier result
     */
    <T> CompletableFuture<T> supplyLater(SchedulerPoll type, Duration delay, ThrowingSupplier<T, Throwable> supplier);

    void shutdown();

}
