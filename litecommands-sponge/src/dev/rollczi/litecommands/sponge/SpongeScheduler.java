package dev.rollczi.litecommands.sponge;

import dev.rollczi.litecommands.scheduler.Scheduler;
import dev.rollczi.litecommands.scheduler.SchedulerPoll;
import dev.rollczi.litecommands.shared.ThrowingSupplier;
import org.spongepowered.api.Game;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.plugin.PluginContainer;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class SpongeScheduler implements Scheduler {

    private final PluginContainer plugin;
    private final Game game;

    public SpongeScheduler(PluginContainer plugin, Game game) {
        this.plugin = plugin;
        this.game = game;
    }

    @Override
    public <T> CompletableFuture<T> supplyLater(SchedulerPoll type, Duration delay, ThrowingSupplier<T, Throwable> supplier) {
        CompletableFuture<T> future = new CompletableFuture<>();

        Task.Builder builder = Task.builder().plugin(plugin).execute(() -> {
            try {
                future.complete(supplier.get());
            } catch (Throwable e) {
                future.completeExceptionally(e);
                if (type.isLogging()) {
                    plugin.logger().error("Error completing command future:", e);
                }
            }
        });

        if (delay.isPositive()) {
            builder.delay(delay);
        }

        if (type.resolve(SchedulerPoll.MAIN, SchedulerPoll.ASYNCHRONOUS).equals(SchedulerPoll.ASYNCHRONOUS)) {
            game.asyncScheduler().submit(builder.build());
        } else {
            game.server().scheduler().submit(builder.build());
        }

        return future;
    }

    @Override
    public void shutdown() {
    }
}
