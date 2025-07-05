package dev.rollczi.litecommands.folia;

import dev.rollczi.litecommands.LiteCommandsBuilder;
import io.papermc.paper.threadedregions.scheduler.AsyncScheduler;
import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;
import io.papermc.paper.threadedregions.scheduler.RegionScheduler;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;

class FoliaInternalExtension {

    static void enableFoliaSupport(Plugin plugin, LiteCommandsBuilder<?, ?, ?> builder, boolean useRegionSchedulerIfAvailable) {
        Server server = plugin.getServer();
        builder
            .scheduler(new FoliaScheduler(plugin))
            .bind(AsyncScheduler.class, () -> server.getAsyncScheduler())
            .bind(GlobalRegionScheduler.class, () -> server.getGlobalRegionScheduler())
            .bind(RegionScheduler.class, () -> server.getRegionScheduler());

        if (useRegionSchedulerIfAvailable) {
            builder.listener(new FoliaSchedulerExecutionController());
        }
    }

}
