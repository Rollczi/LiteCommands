package dev.rollczi.litecommands.scheduler;

import dev.rollczi.litecommands.shared.ThrowingSupplier;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class SchedulerExecutorPoolImpl implements Scheduler {

    private static final String MAIN_THREAD_NAME_FORMAT = "scheduler-%s-main";
    private static final String ASYNC_THREAD_NAME_FORMAT = "scheduler-%s-async-%d";

    private final ThreadLocal<Boolean> isMainThread = ThreadLocal.withInitial(() -> false);

    private final ScheduledExecutorService mainExecutor;
    private final ScheduledExecutorService asyncExecutor;

    public SchedulerExecutorPoolImpl(String name) {
        this(name, Runtime.getRuntime().availableProcessors());
    }

    public SchedulerExecutorPoolImpl(String name, int asyncThreads) {
        this.mainExecutor = Executors.newSingleThreadScheduledExecutor(runnable -> {
            Thread thread = new Thread(runnable);
            thread.setName(String.format(MAIN_THREAD_NAME_FORMAT, name));

            return thread;
        });
        this.mainExecutor.submit(() -> isMainThread.set(true));

        AtomicInteger asyncCount = new AtomicInteger();
        this.asyncExecutor = Executors.newScheduledThreadPool(asyncThreads, runnable -> {
            Thread thread = new Thread(runnable);
            thread.setName(String.format(ASYNC_THREAD_NAME_FORMAT, name, asyncCount.getAndIncrement()));

            return thread;
        });
    }

    @Override
    public <T> CompletableFuture<T> supplyLater(SchedulerPoll type, Duration delay, ThrowingSupplier<T, Throwable> supplier) {
        SchedulerPoll resolve = type.resolve(SchedulerPoll.MAIN, SchedulerPoll.ASYNCHRONOUS);
        CompletableFuture<T> future = new CompletableFuture<>();

        if (resolve.equals(SchedulerPoll.MAIN) && delay.isZero() && isMainThread.get()) {
            return tryRun(supplier, future);
        }

        ScheduledExecutorService executor = resolve.equals(SchedulerPoll.MAIN) ? mainExecutor : asyncExecutor;

        if (delay.isZero()) {
            executor.submit(() -> tryRun(supplier, future));
        }
        else {
            executor.schedule(() -> tryRun(supplier, future), delay.toMillis(), TimeUnit.MILLISECONDS);
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
