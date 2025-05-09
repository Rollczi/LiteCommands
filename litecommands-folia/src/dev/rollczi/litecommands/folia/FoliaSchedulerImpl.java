package dev.rollczi.litecommands.folia;

import dev.rollczi.litecommands.scheduler.AbstractMainThreadBasedScheduler;
import io.papermc.paper.threadedregions.scheduler.AsyncScheduler;
import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

class FoliaSchedulerImpl extends AbstractMainThreadBasedScheduler {

    private final GlobalRegionScheduler globalScheduler;
    private final AsyncScheduler asyncScheduler;
    private final Plugin plugin;

    FoliaSchedulerImpl(Plugin plugin) {
        this.plugin = plugin;
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
        if (isGlobalRegionThread() && delay.isZero()) {
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

    private boolean isGlobalRegionThread() {
        try {
            Class<?> regionizedServer = Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            Method getGlobalThread = regionizedServer.getDeclaredMethod("getGlobalRegionThread");
            Thread globalThread = (Thread) getGlobalThread.invoke(null);
            return Thread.currentThread() == globalThread;
        } catch (ClassNotFoundException e) {
            this.plugin.getLogger().warning("Folia RegionizedServer class not found. Falling back to non-global region thread.");
            return false;
        } catch (NoSuchMethodException e) {
            this.plugin.getLogger().warning("Folia getGlobalRegionThread method not found.");
            return false;
        } catch (IllegalAccessException | InvocationTargetException e) {
            this.plugin.getLogger().log(Level.SEVERE, "Failed to invoke getGlobalRegionThread", e);
            return false;
        }
    }
}