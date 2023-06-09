package dev.rollczi.litecommands.scheduler;

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

    @Override
    public void sync(Runnable runnable) {
        if (isMainThread.get()) {
            runnable.run();
            return;
        }

        mainExecutor.submit(() -> {
            if (!isMainThread.get()) {
                throw new IllegalStateException("Main thread is not main!");
            }

            runnable.run();
        });
    }

    @Override
    public void async(Runnable runnable) {
        asyncExecutor.submit(runnable);
    }

    @Override
    public void shutdown() {
        mainExecutor.shutdown();
        asyncExecutor.shutdown();
    }

}
