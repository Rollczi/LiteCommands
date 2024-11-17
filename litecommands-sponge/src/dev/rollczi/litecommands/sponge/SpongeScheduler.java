package dev.rollczi.litecommands.sponge;

import dev.rollczi.litecommands.scheduler.AbstractMainThreadBasedScheduler;
import org.spongepowered.api.Game;
import org.spongepowered.api.scheduler.Scheduler;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.plugin.PluginContainer;

import java.time.Duration;

public class SpongeScheduler extends AbstractMainThreadBasedScheduler {

    private final PluginContainer pluginContainer;
    private final Scheduler mainScheduler;
    private final Scheduler asyncScheduler;

    public SpongeScheduler(PluginContainer pluginContainer, Game game) {
        this.pluginContainer = pluginContainer;
        this.mainScheduler = findMainScheduler(game);
        this.asyncScheduler = game.asyncScheduler();
    }

    @Override
    public void shutdown() {
    }

    @Override
    protected void runSynchronous(Runnable task, Duration delay) {
        mainScheduler.submit(createTask(task, delay));
    }

    @Override
    protected void runAsynchronous(Runnable task, Duration delay) {
        asyncScheduler.submit(createTask(task, delay));
    }

    @Override
    protected void log(Throwable throwable) {
        pluginContainer.logger().error("Error completing command future:", throwable);
    }

    private Task createTask(Runnable task, Duration delay) {
        return Task.builder()
            .plugin(pluginContainer)
            .execute(task)
            .delay(delay.isPositive() ? delay : Duration.ZERO)
            .build();
    }

    private Scheduler findMainScheduler(Game game) {
        if (game.isServerAvailable()) {
            return game.server().scheduler();
        } else if (game.isClientAvailable()) {
            return game.client().scheduler();
        } else {
            throw new IllegalStateException("Nor server or client are available");
        }
    }
}
