package dev.rollczi.litecommands.bukkit;

import dev.rollczi.litecommands.scheduler.AbstractMainThreadBasedScheduler;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.time.Duration;

public class BukkitScheduler extends AbstractMainThreadBasedScheduler {

    private final org.bukkit.scheduler.BukkitScheduler bukkitScheduler;
    private final Plugin plugin;

    public BukkitScheduler(Plugin plugin) {
        this.bukkitScheduler = plugin.getServer().getScheduler();
        this.plugin = plugin;
    }

    public BukkitScheduler(org.bukkit.scheduler.BukkitScheduler bukkitScheduler, Plugin plugin) {
        this.bukkitScheduler = bukkitScheduler;
        this.plugin = plugin;
    }

    @Override
    public void shutdown() {
    }

    @Override
    protected void runSynchronous(Runnable task, Duration delay) {
        if (Bukkit.isPrimaryThread() && delay.isZero()) {
            task.run();
            return;
        }

        if (delay.isZero()) {
            bukkitScheduler.runTask(plugin, task);
        } else {
            bukkitScheduler.runTaskLater(plugin, task, toTicks(delay));
        }
    }

    @Override
    protected void runAsynchronous(Runnable task, Duration delay) {
        if (delay.isZero()) {
            bukkitScheduler.runTaskAsynchronously(plugin, task);
        } else {
            bukkitScheduler.runTaskLaterAsynchronously(plugin, task, toTicks(delay));
        }
    }

    @Override
    protected void log(Throwable throwable) {
        this.plugin.getLogger().log(Level.SEVERE, "An error occurred while executing a task", throwable);
    }

    private long toTicks(Duration duration) {
        return duration.toMillis() / 50;
    }

}
