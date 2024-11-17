package dev.rollczi.litecommands.sponge;

import dev.rollczi.litecommands.scheduler.AbstractMainThreadBasedScheduler;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.plugin.PluginContainer;

import java.time.Duration;

public class SpongeScheduler extends AbstractMainThreadBasedScheduler {

    private final PluginContainer pluginContainer;
    private final org.spongepowered.api.scheduler.Scheduler mainScheduler;
    private final org.spongepowered.api.scheduler.Scheduler asyncScheduler;

    public SpongeScheduler(PluginContainer pluginContainer, org.spongepowered.api.scheduler.Scheduler mainScheduler, org.spongepowered.api.scheduler.Scheduler asyncScheduler) {
        this.pluginContainer = pluginContainer;
        this.mainScheduler = mainScheduler;
        this.asyncScheduler = asyncScheduler;
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
        Task.Builder builder = Task.builder().plugin(pluginContainer).execute(task);
        if (delay.isPositive()) {
            builder.delay(delay);
        }
        return builder.build();
    }
}
