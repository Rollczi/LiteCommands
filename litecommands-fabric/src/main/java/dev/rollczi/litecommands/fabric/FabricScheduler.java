package dev.rollczi.litecommands.fabric;

import dev.rollczi.litecommands.scheduler.Scheduler;
import dev.rollczi.litecommands.scheduler.SchedulerExecutorPoolImpl;
import dev.rollczi.litecommands.scheduler.SchedulerPoll;
import dev.rollczi.litecommands.shared.ThrowingSupplier;
import net.minecraft.util.thread.ReentrantThreadExecutor;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public abstract class FabricScheduler<R extends Runnable> implements Scheduler {
    private final Supplier<Boolean> isMainThread;

    private final ScheduledExecutorService mainExecutor;
    private final ScheduledExecutorService asyncExecutor;

    public FabricScheduler(Supplier<Boolean> isMainThread, int pool) {
        this.isMainThread = isMainThread;
        this.mainExecutor = Executors.newSingleThreadScheduledExecutor(runnable -> {
            Thread thread = new Thread(runnable);
            thread.setName("scheduler-litecommands-fabric-main");
            return thread;
        });
        AtomicInteger asyncCount = new AtomicInteger();
        ThreadFactory factory = runnable -> {
            Thread thread = new Thread(runnable);
            thread.setName(String.format("scheduler-litecommands-fabric-async-%d", asyncCount.getAndIncrement()));

            return thread;
        };

        this.asyncExecutor = Executors.newScheduledThreadPool(
            Math.max(pool, Runtime.getRuntime().availableProcessors()) / 2,
            factory
        );
    }

    @Override
    public <T> CompletableFuture<T> supplyLater(SchedulerPoll type, Duration delay, ThrowingSupplier<T, Throwable> supplier) {
        CompletableFuture<T> future = new CompletableFuture<>();
        SchedulerPoll resolve = type.resolve(SchedulerPoll.MAIN, SchedulerPoll.ASYNCHRONOUS);
        if (resolve.equals(SchedulerPoll.MAIN) && delay.isZero() && isMainThread.get()) {
            return tryRun(supplier, future);
        } else {
            submit(resolve, delay, supplier, future);
        }
        return future;
    }

    protected static <T> CompletableFuture<T> tryRun(ThrowingSupplier<T, @NotNull Throwable> supplier, CompletableFuture<T> future) {
        try {
            future.complete(supplier.get());
        } catch (Throwable e) {
            future.completeExceptionally(e);
        }
        return future;
    }

    public <T> void submit(SchedulerPoll type, Duration delay, ThrowingSupplier<T, @NotNull Throwable> supplier, CompletableFuture<T> future) {
        if (type.equals(SchedulerPoll.ASYNCHRONOUS)) {
            asyncExecutor.schedule(() -> {
                tryRun(supplier, future);
            }, delay.toMillis(), TimeUnit.MILLISECONDS);
        } else if (delay.isZero()) {
            getExecutor().execute(() -> tryRun(supplier, future));
        } else {
            getExecutor().execute(new LaterTask<>(delay, supplier, future));
        }
    }

    @Override
    public void shutdown() {
        SchedulerExecutorPoolImpl.close(asyncExecutor);
    }

    public abstract ReentrantThreadExecutor<R> getExecutor();

    private class LaterTask<T> implements Runnable {
        private final ThrowingSupplier<T, @NotNull Throwable> supplier;
        private final CompletableFuture<T> future;

        private final long end;

        private LaterTask(Duration delay, ThrowingSupplier<T, @NotNull Throwable> supplier, CompletableFuture<T> future) {
            this.supplier = supplier;
            this.future = future;
            end = System.currentTimeMillis() + delay.toMillis();
        }

        @Override
        public void run() {
            if (!hasNext()) {
                tryRun(supplier, future);
                return;
            }
            // Submit to the main thread
            mainExecutor.submit(() -> {
                getExecutor().submit(this);
            });
        }

        boolean hasNext() {
            return end > System.currentTimeMillis();
        }
    }
}
