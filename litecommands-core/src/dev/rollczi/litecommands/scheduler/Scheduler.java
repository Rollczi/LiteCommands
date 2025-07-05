package dev.rollczi.litecommands.scheduler;

import dev.rollczi.litecommands.shared.ThrowingRunnable;
import dev.rollczi.litecommands.shared.ThrowingSupplier;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

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
    default CompletableFuture<Void> run(SchedulerType type, ThrowingRunnable<Throwable> runnable) {
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
    default CompletableFuture<Void> runLater(SchedulerType type, Duration delay, ThrowingRunnable<Throwable> runnable) {
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
    default <T> CompletableFuture<T> supply(SchedulerType type, ThrowingSupplier<T, Throwable> supplier) {
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
    <T> CompletableFuture<T> supplyLater(SchedulerType type, Duration delay, ThrowingSupplier<T, Throwable> supplier);

    void shutdown();

}
