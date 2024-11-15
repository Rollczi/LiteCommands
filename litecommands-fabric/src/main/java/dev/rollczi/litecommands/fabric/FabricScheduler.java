package dev.rollczi.litecommands.fabric;

import dev.rollczi.litecommands.scheduler.SchedulerMainThreadBased;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import net.minecraft.util.thread.ReentrantThreadExecutor;

public abstract class FabricScheduler<R extends Runnable> extends SchedulerMainThreadBased {

    private final ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
    private final ExecutorService asyncExecutor;

    protected FabricScheduler() {
        this.asyncExecutor = Executors.newFixedThreadPool(Math.max(1, Runtime.getRuntime().availableProcessors() / 2));
    }

    protected FabricScheduler(ExecutorService asyncExecutor) {
        this.asyncExecutor = asyncExecutor;
    }

    @Override
    protected void runSynchronous(Runnable task, Duration delay) {
        if (delay.isZero()) {
            this.getMainThreadExecutor().submit(task);
        } else {
            this.scheduledExecutor.schedule(() -> this.getMainThreadExecutor().submit(task), delay.toMillis(), TimeUnit.MILLISECONDS);
        }
    }

    @Override
    protected void runAsynchronous(Runnable task, Duration delay) {
        if (delay.isZero()) {
            this.getMainThreadExecutor().submit(task);
        } else {
            this.scheduledExecutor.schedule(() -> this.getMainThreadExecutor().submit(task), delay.toMillis(), TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public void shutdown() {
        this.scheduledExecutor.shutdown();
        this.asyncExecutor.shutdown();
    }

    public abstract ReentrantThreadExecutor<R> getMainThreadExecutor();

}
