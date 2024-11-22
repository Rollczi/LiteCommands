package dev.rollczi.litecommands.scheduler;

import dev.rollczi.litecommands.shared.ThrowingSupplier;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class SchedulerExecutorPoolImpl implements Scheduler {

    public static final int CACHED_POOL = -1;
    private static final int MAIN_POOL_SIZE = 1;

    private static final String MAIN_THREAD_NAME_FORMAT = "scheduler-%s-main";
    private static final String ASYNC_THREAD_NAME_FORMAT = "scheduler-%s-async-%d";

    protected final ThreadLocal<Boolean> isMainThread = ThreadLocal.withInitial(() -> false);

    private final ExecutorService mainExecutor;
    private final ExecutorService asyncExecutor;
    private final ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

    public SchedulerExecutorPoolImpl(String name) {
        this(name, CACHED_POOL);
    }

    public SchedulerExecutorPoolImpl(String name, int pool) {
        this.mainExecutor = createMainExecutor(name);
        this.asyncExecutor = createAsyncExecutor(name, pool);
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

    /**
     * Create the main executor.
     * In some cases, the main executor might never be used. We don't want to have to create a useless thread
     *
     * This may look like a premature optimization, but a typical server has 40+ plugins
     * As the framework spreads, more and more of them can use LiteCommands,
     * so we better handle this situation, there are practically no losses anyway
     *
     * To improve performance executor set {@link SchedulerExecutorPoolImpl#isMainThread} to true.
     *
     * @author BlackBaroness
     * @return the main executor. (Should have one thread)
     */
    protected ExecutorService createMainExecutor(String name) {
        ThreadFactory factory = runnable -> new Thread(() -> {
            isMainThread.set(true);
            runnable.run();
        }, String.format(MAIN_THREAD_NAME_FORMAT, name));

        ThreadPoolExecutor mainExecutor = new ThreadPoolExecutor(MAIN_POOL_SIZE, MAIN_POOL_SIZE,
            1, TimeUnit.HOURS,
            new LinkedBlockingQueue<>(),
            factory
        );

        mainExecutor.allowCoreThreadTimeOut(true);
        return mainExecutor;
    }

    /**
     * Create async executor.
     *
     * @author BlackBaroness
     * @return async executor.
    */
    protected ExecutorService createAsyncExecutor(String name, int pool) {
        AtomicInteger asyncThreadCount = new AtomicInteger();
        ThreadFactory factory = runnable -> new Thread(runnable, String.format(ASYNC_THREAD_NAME_FORMAT, name, asyncThreadCount.getAndIncrement()));

        if (pool < 0) {
            return Executors.newCachedThreadPool(factory);
        }

        return new ThreadPoolExecutor(pool, pool,
            3, TimeUnit.MINUTES,
            new LinkedBlockingQueue<>(),
            factory
        );
    }

}
