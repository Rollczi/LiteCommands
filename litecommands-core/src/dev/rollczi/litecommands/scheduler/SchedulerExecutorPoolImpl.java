package dev.rollczi.litecommands.scheduler;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

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

    private <T> CompletableFuture<T> supplySync(Supplier<T> supplier) {
        if (isMainThread.get()) {
            return CompletableFuture.completedFuture(supplier.get());
        }

        return CompletableFuture.supplyAsync(supplier, mainExecutor);
    }

    private <T> CompletableFuture<T> supplyAsync(Supplier<T> supplier) {
        return CompletableFuture.supplyAsync(supplier, asyncExecutor);
    }

    @Override
    public <T> CompletableFuture<T> supply(SchedulerPoll type, Supplier<T> supplier) {
        SchedulerPoll resolve = type.resolve();

        if (resolve == SchedulerPoll.MAIN) {
            return supplySync(supplier);
        }
        else if (resolve == SchedulerPoll.ASYNCHRONOUS) {
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
