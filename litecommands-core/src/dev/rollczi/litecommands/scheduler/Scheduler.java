package dev.rollczi.litecommands.scheduler;

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
    default CompletableFuture<Void> run(SchedulerPoll type, Runnable runnable) {
        return supply(type, () -> {
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
    <T> CompletableFuture<T> supply(SchedulerPoll type, Supplier<T> supplier);

    void shutdown();

}
