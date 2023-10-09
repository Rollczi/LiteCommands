package dev.rollczi.litecommands.scheduler;

import dev.rollczi.litecommands.shared.ThrowingSupplier;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class SchedulerSameThreadImpl implements Scheduler {


    @Override
    public <T> CompletableFuture<T> supplyLater(SchedulerPoll type, Duration delay, ThrowingSupplier<T, Throwable> supplier) {
        CompletableFuture<T> future = new CompletableFuture<>();

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
    }

}
