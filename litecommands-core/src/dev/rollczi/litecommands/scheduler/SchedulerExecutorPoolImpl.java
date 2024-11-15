package dev.rollczi.litecommands.scheduler;

import dev.rollczi.litecommands.shared.ThrowingSupplier;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
public class SchedulerExecutorPoolImpl implements Scheduler {


    private static final String MAIN_THREAD_NAME_FORMAT = "scheduler-%s-main";
    private static final String ASYNC_THREAD_NAME_FORMAT = "scheduler-%s-async-%d";

    private final ThreadLocal<Boolean> isMainThread = ThreadLocal.withInitial(() -> false);

    private final ExecutorService mainExecutor;
    private final ExecutorService asyncExecutor;
    private final ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

    public SchedulerExecutorPoolImpl(String name) {
        this(name, -1);
    }

    public SchedulerExecutorPoolImpl(String name, int pool) {
        this.mainExecutor = Executors.newSingleThreadScheduledExecutor(runnable -> {
            Thread thread = new Thread(runnable);
            thread.setName(String.format(MAIN_THREAD_NAME_FORMAT, name));

            return thread;
        });

        this.mainExecutor.submit(() -> isMainThread.set(true));

        AtomicInteger asyncCount = new AtomicInteger();
        ThreadFactory factory = runnable -> {
            Thread thread = new Thread(runnable);
            thread.setName(String.format(ASYNC_THREAD_NAME_FORMAT, name, asyncCount.getAndIncrement()));

            return thread;
        };

        this.asyncExecutor = pool < 0 ? Executors.newCachedThreadPool(factory) : Executors.newFixedThreadPool(pool, factory);
    }

    @Override
    public <T> CompletableFuture<T> supplyLater(SchedulerPoll type, Duration delay, ThrowingSupplier<T, Throwable> supplier) {
        SchedulerPoll resolve = type.resolve(SchedulerPoll.MAIN, SchedulerPoll.ASYNCHRONOUS);
        CompletableFuture<T> future = new CompletableFuture<>();

        if (resolve.equals(SchedulerPoll.MAIN) && delay.isZero() && isMainThread.get()) {
            return tryRun(supplier, future);
        }

        ExecutorService executor = resolve.equals(SchedulerPoll.MAIN) ? mainExecutor : asyncExecutor;

        if (delay.isZero()) {
            executor.submit(() -> tryRun(supplier, future));
        }
        else {
            scheduledExecutor.schedule(() -> {
                executor.submit(() -> tryRun(supplier, future));
            }, delay.toMillis(), TimeUnit.MILLISECONDS);
        }

        return future;
    }

    private static <T> CompletableFuture<T> tryRun(ThrowingSupplier<T, Throwable> supplier, CompletableFuture<T> future) {
        try {
            future.complete(supplier.get());
        }
        catch (Throwable throwable) {
            future.completeExceptionally(throwable);
        }

        return future;
    }

    @Override
    public void shutdown() {
        mainExecutor.shutdown();
        asyncExecutor.shutdown();
        isMainThread.remove();
    }

}
