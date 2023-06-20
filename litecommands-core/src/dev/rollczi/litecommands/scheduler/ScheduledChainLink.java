package dev.rollczi.litecommands.scheduler;

/**
 * Represents the task in the chain
 *
 * @param <T> the type of the result
 */
public interface ScheduledChainLink<T> {

    /**
     * Executes the task and returns the result
     *
     * @throws ScheduledChainException if the chain is broken
     * @return the result of the task
     */
    T call();

    SchedulerPollType type();

}
