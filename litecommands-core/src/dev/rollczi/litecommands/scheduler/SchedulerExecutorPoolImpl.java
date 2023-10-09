package dev.rollczi.litecommands.scheduler;

import dev.rollczi.litecommands.shared.ThrowingSupplier;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class SchedulerExecutorPoolImpl implements Scheduler {

    private static final String MAIN_THREAD_NAME_FORMAT = "scheduler-%s-main";
    private static final String ASYNC_THREAD_NAME_FORMAT = "scheduler-%s-async-%d";

    private final ThreadLocal<Boolean> isMainThread = ThreadLocal.withInitial(() -> false);

    private final ExecutorService mainExecutor;
    private final ExecutorService asyncExecutor;


    public SchedulerExecutorPoolImpl(String name) {
        this(name, Runtime.getRuntime().availableProcessors());
    }

    public SchedulerExecutorPoolImpl(String name, int asyncThreads) {
        this.mainExecutor = Executors.newSingleThreadExecutor(runnable -> {
            Thread thread = new Thread(runnable);
            thread.setName(String.format(MAIN_THREAD_NAME_FORMAT, name));

            return thread;
        });
        this.mainExecutor.submit(() -> isMainThread.set(true));

        AtomicInteger asyncCount = new AtomicInteger();
        this.asyncExecutor = Executors.newFixedThreadPool(asyncThreads, runnable -> {
            Thread thread = new Thread(runnable);
            thread.setName(String.format(ASYNC_THREAD_NAME_FORMAT, name, asyncCount.getAndIncrement()));

            return thread;
        });
    }

    private <T> CompletableFuture<T> supplySync(ThrowingSupplier<T, Throwable> supplier) {
        CompletableFuture<T> future = new CompletableFuture<>();

        if (isMainThread.get()) {
            try {
                future.complete(supplier.get());
            }
            catch (Throwable throwable) {
                future.completeExceptionally(throwable);
            }

        }
        else {
            mainExecutor.submit(() -> {
                try {
                    future.complete(supplier.get());
                }
                catch (Throwable throwable) {
                    future.completeExceptionally(throwable);
                }
            });
        }

        return future;
    }

    private <T> CompletableFuture<T> supplyAsync(ThrowingSupplier<T, Throwable> supplier) {
        CompletableFuture<T> future = new CompletableFuture<>();

        asyncExecutor.submit(() -> {
            try {
                future.complete(supplier.get());
            }
            catch (Throwable throwable) {
                future.completeExceptionally(throwable);
            }
        });

        return future;
    }

    @Override
    public <T> CompletableFuture<T> supplyLater(SchedulerPoll type, Duration delay, ThrowingSupplier<T, Throwable> supplier) {
        SchedulerPoll resolve = type.resolve(SchedulerPoll.MAIN, SchedulerPoll.ASYNCHRONOUS);

        if (resolve.equals(SchedulerPoll.MAIN)) {
            return supplySync(supplier);
        }
        else if (resolve.equals(SchedulerPoll.ASYNCHRONOUS)) {
            return supplyAsync(supplier);
        }

        throw new IllegalStateException("Cannot resolve the thread type");
    }

    @Override
    public void shutdown() {
        mainExecutor.shutdown();
        asyncExecutor.shutdown();
    }

}
