package dev.rollczi.litecommands.scheduler;

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
    void sync(Runnable runnable);

    /**
     * Runs the runnable in the async thread,
     * if the current thread is the async thread, the runnable will be executed immediately
     *
     * @see Scheduler#sync(Runnable)
     * @param runnable the runnable to run
     */
    void async(Runnable runnable);

    /**
     * Runs the runnable in the async or main thread,
     * if the current thread is the async thread, the runnable will be executed immediately
     *
     * @param type the type of the thread to run the runnable
     * @param runnable the runnable to run
     */
    default void schedule(SchedulerTaskType type, Runnable runnable) {
        switch (type) {
            case SYNC: sync(runnable); break;
            case ASYNC: async(runnable); break;
        }
    }

    void shutdown();

}
