package dev.rollczi.litecommands.folia;

import dev.rollczi.litecommands.scheduler.AbstractMainThreadBasedScheduler;
import dev.rollczi.litecommands.scheduler.SchedulerType;
import io.papermc.paper.threadedregions.scheduler.AsyncScheduler;
import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;
import io.papermc.paper.threadedregions.scheduler.RegionScheduler;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

class FoliaScheduler extends AbstractMainThreadBasedScheduler {

    private final GlobalRegionScheduler globalScheduler;
    private final AsyncScheduler asyncScheduler;
    private final Plugin plugin;
    private final RegionScheduler regionScheduler;

    FoliaScheduler(Plugin plugin) {
        this.plugin = plugin;
        this.regionScheduler = plugin.getServer().getRegionScheduler();
        this.globalScheduler = plugin.getServer().getGlobalRegionScheduler();
        this.asyncScheduler = plugin.getServer().getAsyncScheduler();
    }

    @Override
    public void shutdown() {
        globalScheduler.cancelTasks(plugin);
        asyncScheduler.cancelTasks(plugin);
    }

    @Override
    protected void runSynchronous(Runnable task, Duration delay) {
        if (plugin.getServer().isGlobalTickThread() && delay.isZero()) {
            task.run();
            return;
        }

        if (delay.isZero()) {
            globalScheduler.execute(plugin, task);
        } else {
            globalScheduler.runDelayed(plugin, (scheduledTask) -> task.run(), toTicks(delay));
        }
    }

    @Override
    protected void runAsynchronous(Runnable task, Duration delay) {
        if (delay.isZero()) {
            asyncScheduler.runNow(plugin, (scheduledTask) -> task.run());
        } else {
            asyncScheduler.runDelayed(plugin, (scheduledTask) -> task.run(), delay.toMillis(), TimeUnit.MILLISECONDS);
        }
    }

    @Override
    protected void log(Throwable throwable) {
        this.plugin.getLogger().log(Level.SEVERE, "An error occurred while executing a task", throwable);
    }

    private long toTicks(Duration duration) {
        return duration.toMillis() / 50;
    }

    @Override
    protected boolean runUnknown(SchedulerType type, Duration delay, Runnable task) {
        if (!(type instanceof FoliaSchedulerType foliaSchedulerType)) {
            return false;
        }

        Location location = foliaSchedulerType.getLocation();

        if (delay.isZero()) {
            regionScheduler.execute(plugin, location, task);
        } else {
            regionScheduler.runDelayed(plugin, location, (scheduledTask) -> task.run(), toTicks(delay));
        }
        return true;
    }

}