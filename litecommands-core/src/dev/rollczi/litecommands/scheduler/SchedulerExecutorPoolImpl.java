package dev.rollczi.litecommands.scheduler;

import dev.rollczi.litecommands.shared.ThrowingRunnable;
import dev.rollczi.litecommands.shared.ThrowingSupplier;
import org.jetbrains.annotations.ApiStatus;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class SchedulerExecutorPoolImpl implements Scheduler {

    private final Logger logger;
    private final ScheduledExecutorService mainExecutor;
    private final boolean closeMainExecutorOnShutdown;

    private final ScheduledExecutorService asyncExecutor;
    private final boolean closeAsyncExecutorOnShutdown;

    @Deprecated
    public SchedulerExecutorPoolImpl(String name) {
        this(name, -1);
    }

    @Deprecated
    public SchedulerExecutorPoolImpl(String name, int pool) {
        this.logger = Logger.getLogger("LiteCommands");

        this.mainExecutor = Executors.newSingleThreadScheduledExecutor(runnable -> {
            Thread thread = new Thread(runnable);
            thread.setName(String.format("scheduler-%s-main", name));
            return thread;
        });
        this.closeMainExecutorOnShutdown = true;

        AtomicInteger asyncCount = new AtomicInteger();
        ThreadFactory factory = runnable -> {
            Thread thread = new Thread(runnable);
            thread.setName(String.format("scheduler-%s-async-%d", name, asyncCount.getAndIncrement()));

            return thread;
        };

        this.asyncExecutor = Executors.newScheduledThreadPool(
            Math.max(pool, Runtime.getRuntime().availableProcessors()) / 2,
            factory
        );
        this.closeAsyncExecutorOnShutdown = true;
    }

    /**
     * Internal usage only. Use {@link SchedulerExecutorPoolBuilder}.
     */
    @ApiStatus.Internal
    public SchedulerExecutorPoolImpl(Logger logger, ScheduledExecutorService mainExecutor, boolean closeMainExecutorOnShutdown, ScheduledExecutorService asyncExecutor, boolean closeAsyncExecutorOnShutdown) {
        this.logger = logger;
        this.mainExecutor = mainExecutor;
        this.closeMainExecutorOnShutdown = closeMainExecutorOnShutdown;
        this.asyncExecutor = asyncExecutor;
        this.closeAsyncExecutorOnShutdown = closeAsyncExecutorOnShutdown;
    }

    @Override
    public CompletableFuture<Void> run(SchedulerPoll type, ThrowingRunnable<Throwable> runnable) {
        return CompletableFuture.runAsync(() -> {
            try {
                runnable.run();
            } catch (Throwable e) {
                throw new CompletionException(e);
            }
        }, getSchedulerByPollType(type));
    }

    @Override
    public CompletableFuture<Void> runLater(SchedulerPoll type, Duration delay, ThrowingRunnable<Throwable> runnable) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        getSchedulerByPollType(type).schedule(() -> {
            try {
                runnable.run();
                future.complete(null);
            } catch (Throwable e) {
                future.completeExceptionally(e);
            }
        }, delay.toMillis(), TimeUnit.MILLISECONDS);
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
        }, getSchedulerByPollType(type));
    }

    @Override
    public <T> CompletableFuture<T> supplyLater(SchedulerPoll type, Duration delay, ThrowingSupplier<T, Throwable> supplier) {
        CompletableFuture<T> future = new CompletableFuture<>();
        getSchedulerByPollType(type).schedule(() -> {
            try {
                future.complete(supplier.get());
            } catch (Throwable e) {
                future.completeExceptionally(e);
            }
        }, delay.toMillis(), TimeUnit.MILLISECONDS);
        return future;
    }

    @Override
    public void shutdown() {
        if (closeMainExecutorOnShutdown) {
            try {
                mainExecutor.close();
            } catch (Throwable e) {
                logger.severe("Error closing main executor: \n" + getStacktraceAsString(e));
            }
        }

        if (closeAsyncExecutorOnShutdown) {
            try {
                asyncExecutor.close();
            } catch (Throwable e) {
                logger.severe("Error closing async executor: \n" + getStacktraceAsString(e));
            }
        }
    }

    private ScheduledExecutorService getSchedulerByPollType(SchedulerPoll type) {
        return type.resolve(SchedulerPoll.MAIN, SchedulerPoll.ASYNCHRONOUS).equals(SchedulerPoll.MAIN)
            ? mainExecutor
            : asyncExecutor;
    }

    private String getStacktraceAsString(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

}
