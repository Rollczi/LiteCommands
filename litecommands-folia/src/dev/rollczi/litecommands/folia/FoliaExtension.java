package dev.rollczi.litecommands.folia;

import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.LiteCommandsInternal;
import dev.rollczi.litecommands.bukkit.Environment;
import dev.rollczi.litecommands.configurator.LiteConfigurator;
import dev.rollczi.litecommands.extension.LiteExtension;
import dev.rollczi.litecommands.scheduler.Scheduler;
import java.util.function.Supplier;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

public final class FoliaExtension implements LiteExtension<CommandSender, FoliaExtension.Settings> {

    private final Settings settings;
    private final Plugin plugin;

    public FoliaExtension(Plugin plugin) {
        this.plugin = plugin;
        this.settings = new Settings(true, () -> new dev.rollczi.litecommands.bukkit.BukkitScheduler(plugin));
    }

    @Override
    public void configure(LiteConfigurator<Settings> configurer) {
        configurer.configure(settings);
    }

    @Override
    public void extend(LiteCommandsBuilder<CommandSender, ?, ?> builder, LiteCommandsInternal<CommandSender, ?> internal) {
        if (Environment.isFolia()) {
            FoliaInternalExtension.enableFoliaSupport(plugin, builder, settings.useRegionSchedulerIfAvailable);
            return;
        }

        fallback(builder);
    }

    private void fallback(LiteCommandsBuilder<CommandSender, ?, ?> builder) {
        if (settings.fallbackScheduler != null) {
            builder.scheduler(settings.fallbackScheduler.get());
        }
    }

    public record Settings(
        boolean useRegionSchedulerIfAvailable,
        @Nullable Supplier<Scheduler> fallbackScheduler
    ) {
        public Settings fallbackScheduler(Supplier<Scheduler> fallbackScheduler) {
            return new Settings(this.useRegionSchedulerIfAvailable, fallbackScheduler);
        }
    }

}
