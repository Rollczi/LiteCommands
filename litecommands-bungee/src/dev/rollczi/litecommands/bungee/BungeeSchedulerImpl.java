package dev.rollczi.litecommands.bungee;

import com.google.common.base.Throwables;
import dev.rollczi.litecommands.scheduler.Scheduler;
import dev.rollczi.litecommands.scheduler.SchedulerPoll;
import dev.rollczi.litecommands.shared.ThrowingRunnable;
import dev.rollczi.litecommands.shared.ThrowingSupplier;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * This Scheduler implementation always runs its task in an internal scheduler, no matter what
 * <p>
 * Because BungeeCord doesn't have a main thread, Scheduler is called mainly on Netty threads that
 * should never be running blocking tasks (it will increase the ping of a part of online players and can even kick them)
 * <p>
 * By using that, we are making the entire platform more reliable, especially for developers who don't know about it
 */
public class BungeeSchedulerImpl implements Scheduler {

    private final Logger logger;
    private final ScheduledThreadPoolExecutor executor;
    private final boolean closeExecutorOnShutdown;

    public BungeeSchedulerImpl(Logger logger, ScheduledThreadPoolExecutor executor, boolean closeExecutorOnShutdown) {
        this.logger = logger;
        this.executor = executor;
        this.closeExecutorOnShutdown = closeExecutorOnShutdown;
    }

    public BungeeSchedulerImpl(Logger logger) {
        this.logger = logger;

        /*
        BungeeCord's own scheduler is not efficient because it creates an unlimited cached thread pool for
        each plugin and just uses it:
        https://github.com/SpigotMC/BungeeCord/blob/master/proxy/src/main/java/net/md_5/bungee/scheduler/BungeeScheduler.java
        https://github.com/SpigotMC/BungeeCord/blob/master/api/src/main/java/net/md_5/bungee/api/plugin/Plugin.java

        It also just blocks the threads if a task with a delay is received (by using Thread.sleep),
        so the thread won't be doing anything more

        So, we need to create our own scheduler that should both be fast and using fewer threads
        We use a fixed core pool size to not hold an unreasonable number of threads on server CPUs

        We could also decrease the max cap, but long-running commands are those that use I/O,
        so it's fine and meaningful to create a bigger pool in this case
         */
        this.executor = new ScheduledThreadPoolExecutor(4);
        executor.setMaximumPoolSize(Runtime.getRuntime().availableProcessors() * 2);
        executor.setKeepAliveTime(3, TimeUnit.MINUTES);

        this.closeExecutorOnShutdown = true;
    }

    @Override
    public CompletableFuture<Void> run(SchedulerPoll type, ThrowingRunnable<Throwable> runnable) {
        return CompletableFuture.runAsync(() -> {
            try {
                runnable.run();
            } catch (Throwable e) {
                throw new CompletionException(e);
            }
        }, executor);
    }

    @Override
    public CompletableFuture<Void> runLater(SchedulerPoll type, Duration delay, ThrowingRunnable<Throwable> runnable) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        executor.submit(() -> {
            try {
                runnable.run();
                future.complete(null);
            } catch (Throwable e) {
                future.completeExceptionally(e);
            }
        });
        return future;
    }

    @Override
    public <T> CompletableFuture<T> supply(SchedulerPoll type, ThrowingSupplier<T, Throwable> supplier) {
        return CompletableFuture.supplyAsync(() -> {
            try {
               return supplier.get();
            } catch (Throwable e) {
                throw new CompletionException(e);
            }
        }, executor);
    }

    @Override
    public <T> CompletableFuture<T> supplyLater(SchedulerPoll type, Duration delay, ThrowingSupplier<T, Throwable> supplier) {
        CompletableFuture<T> future = new CompletableFuture<>();
        executor.submit(() -> {
            try {
                supplier.get();
                future.complete(null);
            } catch (Throwable e) {
                future.completeExceptionally(e);
            }
        });
        return future;
    }

    @Override
    public void shutdown() {
        if (closeExecutorOnShutdown) {
            try {
                executor.close();
            } catch (Throwable e) {
                logger.severe("Error closing scheduler executor: \n" + Throwables.getStackTraceAsString(e));
            }
        }
    }
}
