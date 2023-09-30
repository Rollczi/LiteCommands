package dev.rollczi.litecommands.bukkit;

import dev.rollczi.litecommands.scheduler.Scheduler;
import dev.rollczi.litecommands.scheduler.SchedulerPoll;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

class BukkitSchedulerImpl implements Scheduler {

    private final BukkitScheduler bukkitScheduler;
    private final Plugin plugin;

    BukkitSchedulerImpl(BukkitScheduler bukkitScheduler, Plugin plugin) {
        this.bukkitScheduler = bukkitScheduler;
        this.plugin = plugin;
    }

    private <T> CompletableFuture<T> supplySync(Supplier<T> supplier) {
        if (Bukkit.isPrimaryThread()) {
            return runInCurrentThread(supplier);
        }

        CompletableFuture<T> future = new CompletableFuture<>();

        bukkitScheduler.runTask(plugin, () -> {
            try {
                future.complete(supplier.get());
            }
            catch (Throwable throwable) {
                future.completeExceptionally(throwable);
            }
        });

        return future;
    }

    private <T> CompletableFuture<T> supplyAsync(Supplier<T> supplier) {
        if (!Bukkit.isPrimaryThread()) {
            return runInCurrentThread(supplier);
        }

        CompletableFuture<T> future = new CompletableFuture<>();

        bukkitScheduler.runTaskAsynchronously(plugin, () -> {
            try {
                future.complete(supplier.get());
            }
            catch (Throwable throwable) {
                future.completeExceptionally(throwable);
            }
        });

        return future;
    }

    private <T> CompletableFuture<T> runInCurrentThread(Supplier<T> supplier) {
        try {
            return CompletableFuture.completedFuture(supplier.get());
        }
        catch (Throwable throwable) {
            CompletableFuture<T> future = new CompletableFuture<>();

            future.completeExceptionally(throwable);
            return future;
        }
    }

    @Override
    public <T> CompletableFuture<T> supply(SchedulerPoll type, Supplier<T> supplier) {
        SchedulerPoll resolved = type.resolve(SchedulerPoll.MAIN, SchedulerPoll.ASYNCHRONOUS);

        if (resolved == SchedulerPoll.MAIN) {
            return supplySync(supplier);
        }

        if (resolved == SchedulerPoll.ASYNCHRONOUS) {
            return supplyAsync(supplier);
        }

        return supplySync(supplier);
    }

    @Override
    public void shutdown() {
    }

}
