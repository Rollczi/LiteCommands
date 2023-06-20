package dev.rollczi.litecommands.scheduler;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 * Scheduler is used to run the commands in the async or main thread
 */
public interface Scheduler {

    /**
     * Runs the runnable in the main thread,
     * if the current thread is the main thread, the runnable will be executed immediately
     *
     * @see Scheduler#async(Runnable)
     * @param runnable the runnable to run
     */
     default CompletableFuture<Void> sync(Runnable runnable) {
         return supplySync(() -> {
             runnable.run();
             return null;
         });
     }

     <T> CompletableFuture<T> supplySync(Supplier<T> supplier);

    /**
     * Runs the runnable in the async thread,
     * if the current thread is the async thread, the runnable will be executed immediately
     *
     * @see Scheduler#sync(Runnable)
     * @param runnable the runnable to run
     */
    default CompletableFuture<Void> async(Runnable runnable) {
        return supplyAsync(() -> {
            runnable.run();
            return null;
        });
    }

    <T> CompletableFuture<T> supplyAsync(Supplier<T> supplier);

    /**
     * Runs the runnable in the async or main thread,
     * if the current thread is the async thread, the runnable will be executed immediately
     *
     * @param type the type of the thread to run the runnable
     * @param runnable the runnable to run
     */
    default CompletableFuture<Void> run(SchedulerPollType type, Runnable runnable) {
        return supply(type, () -> {
            runnable.run();
            return null;
        });
    }

    default <T> CompletableFuture<T> supply(SchedulerPollType type, Supplier<T> supplier) {
        switch (type) {
            case SYNC: return supplySync(supplier);
            case ASYNC: return supplyAsync(supplier);
            default: throw new IllegalArgumentException("Unknown type: " + type);
        }
    }

    void shutdown();

}
