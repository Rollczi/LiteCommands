package dev.rollczi.litecommands.fabric;

import dev.rollczi.litecommands.scheduler.SchedulerExecutorPoolWrapperImpl;
import dev.rollczi.litecommands.scheduler.SchedulerPoll;
import dev.rollczi.litecommands.shared.ThrowingRunnable;
import dev.rollczi.litecommands.shared.ThrowingSupplier;
import net.minecraft.util.thread.ReentrantThreadExecutor;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public abstract class FabricScheduler<R extends Runnable> extends SchedulerExecutorPoolWrapperImpl {

    public FabricScheduler(int pool) {
        super("litecommands-fabric", pool);
    }

    @Override
    public <T> CompletableFuture<T> supplyLater(SchedulerPoll type, Duration delay, ThrowingSupplier<T, Throwable> supplier) {
        return super.supplyLater(type, delay, new LaterSupplier<>(type.resolve(SchedulerPoll.MAIN, SchedulerPoll.ASYNCHRONOUS), supplier));
    }

    @Override
    public CompletableFuture<Void> run(SchedulerPoll type, ThrowingRunnable<Throwable> runnable) {
        return super.run(type, new LaterRunnable(type.resolve(SchedulerPoll.MAIN, SchedulerPoll.ASYNCHRONOUS), runnable));
    }

    @Override
    public CompletableFuture<Void> runLater(SchedulerPoll type, Duration delay, ThrowingRunnable<Throwable> runnable) {
        return super.runLater(type, delay, new LaterRunnable(type.resolve(SchedulerPoll.MAIN, SchedulerPoll.ASYNCHRONOUS), runnable));
    }

    @Override
    public <T> CompletableFuture<T> supply(SchedulerPoll type, ThrowingSupplier<T, Throwable> supplier) {
        return super.supply(type, new LaterSupplier<>(type.resolve(SchedulerPoll.MAIN, SchedulerPoll.ASYNCHRONOUS), supplier));
    }

    public abstract ReentrantThreadExecutor<R> getMainThreadExecutor();

    private class LaterSupplier<T, E extends Throwable> implements ThrowingSupplier<T, E> {
        private final SchedulerPoll resolve;
        private final ThrowingSupplier<T, E> supplier;

        public LaterSupplier(SchedulerPoll resolve, ThrowingSupplier<T, E> supplier) {
            this.resolve = resolve;
            this.supplier = supplier;
        }

        @Override
        public T get() throws E {
            if (resolve.equals(SchedulerPoll.MAIN)) {
                CompletableFuture<T> future = new CompletableFuture<>();
                //noinspection ResultOfMethodCallIgnored
                getMainThreadExecutor().submit(() -> {
                    try {
                        future.complete(supplier.get());
                    } catch (Throwable e) {
                        future.completeExceptionally(e);
                    }
                });
                return future.join();
            } else {
                return supplier.get();
            }
        }
    }

    private class LaterRunnable implements ThrowingRunnable<Throwable> {
        private final SchedulerPoll resolve;
        private final ThrowingRunnable<Throwable> runnable;

        public LaterRunnable(SchedulerPoll resolve, ThrowingRunnable<Throwable> runnable) {
            this.resolve = resolve;
            this.runnable = runnable;
        }

        @Override
        public void run() throws Throwable {
            if (resolve.equals(SchedulerPoll.MAIN)) {
                try {
                    getMainThreadExecutor().submit(() -> {
                        try {
                            runnable.run();
                        } catch (Throwable e) {
                            throw new RuntimeException(e);
                        }
                    }).join();
                } catch (Throwable e) {
                    if (e instanceof CompletionException) {
                        Throwable cause = e.getCause();
                        if (cause instanceof RuntimeException) {
                            throw cause.getCause();
                        } else {
                            throw cause;
                        }
                    } else {
                        throw e;
                    }
                }
            } else {
                runnable.run();
            }
        }
    }
}
