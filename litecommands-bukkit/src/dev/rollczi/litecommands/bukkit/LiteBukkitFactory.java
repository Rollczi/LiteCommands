package dev.rollczi.litecommands.bukkit;

import dev.rollczi.litecommands.LiteCommandsBaseBuilder;
import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.LiteCommandsFactory;
import dev.rollczi.litecommands.bukkit.argument.LocationArgument;
import dev.rollczi.litecommands.bukkit.argument.NamespacedKeyArgument;
import dev.rollczi.litecommands.bukkit.argument.OfflinePlayerArgument;
import dev.rollczi.litecommands.bukkit.argument.OldEnumAccessor;
import dev.rollczi.litecommands.bukkit.argument.OldEnumArgument;
import dev.rollczi.litecommands.bukkit.argument.PlayerArgument;
import dev.rollczi.litecommands.bukkit.argument.WorldArgument;
import dev.rollczi.litecommands.bukkit.context.ConsoleOnlyContextProvider;
import dev.rollczi.litecommands.bukkit.context.LocationContext;
import dev.rollczi.litecommands.bukkit.context.PlayerOnlyContextProvider;
import dev.rollczi.litecommands.bukkit.context.WorldContext;
import dev.rollczi.litecommands.bukkit.util.BukkitFallbackPrefixUtil;
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

public final class LiteBukkitFactory {

    private LiteBukkitFactory() {
    }

    public static <B extends LiteCommandsBuilder<CommandSender, LiteBukkitSettings, B>> B builder() {
        Plugin plugin = JavaPlugin.getProvidingPlugin(LiteBukkitFactory.class);
        return builder(BukkitFallbackPrefixUtil.create(plugin), plugin, plugin.getServer());
    }

    public static <B extends LiteCommandsBuilder<CommandSender, LiteBukkitSettings, B>> B builder(String fallbackPrefix) {
        Plugin plugin = JavaPlugin.getProvidingPlugin(LiteBukkitFactory.class);
        return builder(fallbackPrefix, plugin, plugin.getServer());
    }

    public static <B extends LiteCommandsBuilder<CommandSender, LiteBukkitSettings, B>> B builder(Plugin plugin) {
        return builder(BukkitFallbackPrefixUtil.create(plugin), plugin, plugin.getServer());
    }

    public static <B extends LiteCommandsBuilder<CommandSender, LiteBukkitSettings, B>> B builder(String fallbackPrefix, Plugin plugin) {
        return builder(fallbackPrefix, plugin, plugin.getServer());
    }

    public static <B extends LiteCommandsBuilder<CommandSender, LiteBukkitSettings, B>> B builder(String fallbackPrefix, Plugin plugin, Server server) {
        LiteBukkitSettings settings = new LiteBukkitSettings(server)
            .fallbackPrefix(fallbackPrefix);

        return builder(plugin, server, settings);
    }


    @SuppressWarnings("unchecked")
    public static <B extends LiteCommandsBuilder<CommandSender, LiteBukkitSettings, B>> B builder(Plugin plugin, Server server, LiteBukkitSettings settings) {
        return (B) builder0(plugin, server, settings);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static <B extends LiteCommandsBaseBuilder<CommandSender, LiteBukkitSettings, B>> B builder0(Plugin plugin, Server server, LiteBukkitSettings settings) {
        return LiteCommandsFactory.<CommandSender, LiteBukkitSettings, B>builder(CommandSender.class, builder -> new BukkitPlatform(settings, builder.getPermissionService())).self((builder, internal) -> {
            MessageRegistry<CommandSender> messageRegistry = internal.getMessageRegistry();

            Class namespacedKeyClass = null;
            try {
                namespacedKeyClass = Class.forName("org.bukkit.NamespacedKey");
            }
            catch (ClassNotFoundException e) {
                // NO-OP
            }

            builder
                .permissionResolver(PermissionResolver.createDefault(CommandSender.class, (sender, permission) -> sender.hasPermission(permission)))

                .bind(Plugin.class, () -> plugin)
                .bind(Server.class, () -> server)
                .bind(org.bukkit.scheduler.BukkitScheduler.class, () -> server.getScheduler())

                .scheduler(new BukkitScheduler(server.getScheduler(), plugin))

                .settings(bukkitSettings -> bukkitSettings.tabCompleter(TabComplete.create(internal.getScheduler(), plugin)))

                .argument(Player.class, new PlayerArgument(server, messageRegistry))
                .argument(World.class, new WorldArgument(server, messageRegistry))
                .argument(Location.class, new LocationArgument(messageRegistry))
                .argument(OfflinePlayer.class, new OfflinePlayerArgument(server, plugin, messageRegistry, settings.isParseUnknownPlayersAllowed(), settings.getPlayerNamePattern()));

            if (namespacedKeyClass != null) {
                builder.argument(namespacedKeyClass, new NamespacedKeyArgument(plugin, messageRegistry));
            }

            builder
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
