package dev.rollczi.litecommands.folia;

import dev.rollczi.litecommands.LiteCommandsBaseBuilder;
import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.LiteCommandsFactory;
import dev.rollczi.litecommands.folia.argument.LocationArgument;
import dev.rollczi.litecommands.folia.argument.OfflinePlayerArgument;
import dev.rollczi.litecommands.folia.argument.OldEnumAccessor;
import dev.rollczi.litecommands.folia.argument.OldEnumArgument;
import dev.rollczi.litecommands.folia.argument.PlayerArgument;
import dev.rollczi.litecommands.folia.argument.WorldArgument;
import dev.rollczi.litecommands.folia.context.ConsoleOnlyContextProvider;
import dev.rollczi.litecommands.folia.context.LocationContext;
import dev.rollczi.litecommands.folia.context.PlayerOnlyContextProvider;
import dev.rollczi.litecommands.folia.context.WorldContext;
import dev.rollczi.litecommands.folia.util.FoliaFallbackPrefixUtil;
import dev.rollczi.litecommands.message.MessageRegistry;
import dev.rollczi.litecommands.permission.PermissionResolver;
import dev.rollczi.litecommands.reflect.type.TypeRange;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public final class LiteFoliaFactory {

    private LiteFoliaFactory() {
    }

    public static <B extends LiteCommandsBuilder<CommandSender, LiteFoliaSettings, B>> B builder() {
        Plugin plugin = JavaPlugin.getProvidingPlugin(LiteFoliaFactory.class);
        return builder(FoliaFallbackPrefixUtil.create(plugin), plugin, plugin.getServer());
    }

    public static <B extends LiteCommandsBuilder<CommandSender, LiteFoliaSettings, B>> B builder(String fallbackPrefix) {
        Plugin plugin = JavaPlugin.getProvidingPlugin(LiteFoliaFactory.class);
        return builder(fallbackPrefix, plugin, plugin.getServer());
    }

    public static <B extends LiteCommandsBuilder<CommandSender, LiteFoliaSettings, B>> B builder(Plugin plugin) {
        return builder(FoliaFallbackPrefixUtil.create(plugin), plugin, plugin.getServer());
    }

    public static <B extends LiteCommandsBuilder<CommandSender, LiteFoliaSettings, B>> B builder(String fallbackPrefix, Plugin plugin) {
        return builder(fallbackPrefix, plugin, plugin.getServer());
    }

    public static <B extends LiteCommandsBuilder<CommandSender, LiteFoliaSettings, B>> B builder(String fallbackPrefix, Plugin plugin, Server server) {
        LiteFoliaSettings settings = new LiteFoliaSettings(server)
            .fallbackPrefix(fallbackPrefix);

        return builder(plugin, server, settings);
    }


    @SuppressWarnings("unchecked")
    public static <B extends LiteCommandsBuilder<CommandSender, LiteFoliaSettings, B>> B builder(Plugin plugin, Server server, LiteFoliaSettings settings) {
        return (B) builder0(plugin, server, settings);
    }

    @SuppressWarnings("unchecked")
    private static <B extends LiteCommandsBaseBuilder<CommandSender, LiteFoliaSettings, B>> B builder0(Plugin plugin, Server server, LiteFoliaSettings settings) {
        return LiteCommandsFactory.<CommandSender, LiteFoliaSettings, B>builder(CommandSender.class, builder -> new FoliaPlatform(settings, builder.getPermissionService())).self((builder, internal) -> {
            MessageRegistry<CommandSender> messageRegistry = internal.getMessageRegistry();

            builder
                .permissionResolver(PermissionResolver.createDefault(CommandSender.class, (sender, permission) -> sender.hasPermission(permission)))

                .bind(Plugin.class, () -> plugin)
                .bind(Server.class, () -> server)
                .bind(BukkitScheduler.class, () -> server.getScheduler())

                .scheduler(new FoliaSchedulerImpl(plugin))

                .settings(bukkitSettings -> bukkitSettings.tabCompleter(TabComplete.create(internal.getScheduler(), plugin)))

                .argument(Player.class, new PlayerArgument(server, messageRegistry))
                .argument(World.class, new WorldArgument(server, messageRegistry))
                .argument(Location.class, new LocationArgument(messageRegistry))
                .argument(OfflinePlayer.class, new OfflinePlayerArgument(server, plugin, true))

                .context(Player.class, new PlayerOnlyContextProvider(messageRegistry))
                .context(ConsoleCommandSender.class, new ConsoleOnlyContextProvider(messageRegistry))
                .context(World.class, new WorldContext(messageRegistry))
                .context(Location.class, new LocationContext(messageRegistry))

                .result(String.class, new StringHandler());

            if (OldEnumAccessor.isAvailable()) {
                TypeRange<Object> upwards = (TypeRange<Object>) TypeRange.upwards(OldEnumAccessor.getTypeOrThrow());
                builder.advanced().argument(upwards, new OldEnumArgument());
            }
        });
    }

}
