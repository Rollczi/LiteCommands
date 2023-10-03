package dev.rollczi.litecommands.bukkit;

import dev.rollczi.litecommands.scheduler.Scheduler;
import dev.rollczi.litecommands.scheduler.SchedulerPoll;
import dev.rollczi.litecommands.shared.ThrowingSupplier;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

class BukkitSchedulerImpl implements Scheduler {

    private final BukkitScheduler bukkitScheduler;
    private final Plugin plugin;

    BukkitSchedulerImpl(BukkitScheduler bukkitScheduler, Plugin plugin) {
        this.bukkitScheduler = bukkitScheduler;
        this.plugin = plugin;
    }

    @Override
    public <T> CompletableFuture<T> supplyLater(SchedulerPoll type, Duration delay, ThrowingSupplier<T, Throwable> supplier) {
        SchedulerPoll resolved = type.resolve(SchedulerPoll.MAIN, SchedulerPoll.ASYNCHRONOUS);

        if (resolved.equals(SchedulerPoll.MAIN)) {
            return supplySync(type, supplier, delay);
        }

        if (resolved.equals(SchedulerPoll.ASYNCHRONOUS)) {
            return supplyAsync(type, supplier, delay);
        }

        throw new IllegalArgumentException("Unknown scheduler poll type: " + type);
    }

    @Override
    public void shutdown() {
    }

    private <T> CompletableFuture<T> supplySync(SchedulerPoll type, ThrowingSupplier<T, Throwable> supplier, Duration delay) {
        CompletableFuture<T> future = new CompletableFuture<>();

        if (Bukkit.isPrimaryThread()) {
            return tryRun(type, future, supplier);
        }

        if (delay.isZero()) {
            bukkitScheduler.runTask(plugin, () -> tryRun(type, future, supplier));
        }
        else {
            bukkitScheduler.runTaskLater(plugin, () -> tryRun(type, future, supplier), toTicks(delay));
        }

        return future;
    }

    private <T> CompletableFuture<T> supplyAsync(SchedulerPoll type, ThrowingSupplier<T, Throwable> supplier, Duration delay) {
        CompletableFuture<T> future = new CompletableFuture<>();

        if (delay.isZero()) {
            bukkitScheduler.runTaskAsynchronously(plugin, () -> tryRun(type, future, supplier));
        }
        else {
            bukkitScheduler.runTaskLaterAsynchronously(plugin, () -> tryRun(type, future, supplier), toTicks(delay));
        }

        return future;
    }

    private <T> CompletableFuture<T> tryRun(SchedulerPoll type, CompletableFuture<T> future, ThrowingSupplier<T, Throwable> supplier) {
        try {
            future.complete(supplier.get());
        }
        catch (Throwable throwable) {
            future.completeExceptionally(throwable);

            if (type.isLogging()) {
                throwable.printStackTrace();
            }
        }

        return future;
    }

    private long toTicks(Duration duration) {
        return duration.toMillis() / 50;
    }

}
