package dev.rollczi.litecommands.scheduler;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class SchedulerSameThreadImpl implements Scheduler {

    @Override
    public <T> CompletableFuture<T> supplySync(Supplier<T> supplier) {
        return CompletableFuture.completedFuture(supplier.get());
    }

    @Override
    public <T> CompletableFuture<T> supplyAsync(Supplier<T> supplier) {
        return CompletableFuture.completedFuture(supplier.get());
    }

    @Override
    public void shutdown() {
    }

}
