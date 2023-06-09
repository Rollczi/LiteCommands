package dev.rollczi.litecommands.scheduler;

public class SchedulerSameThreadImpl implements Scheduler {

    @Override
    public void sync(Runnable runnable) {
        runnable.run();
    }

    @Override
    public void async(Runnable runnable) {
        runnable.run();
    }

    @Override
    public void shutdown() {
    }

}
