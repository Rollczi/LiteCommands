package dev.rollczi.litecommands.bukkit;

import dev.rollczi.litecommands.LiteCommandsFactory;
import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.bukkit.argument.LocationArgument;
import dev.rollczi.litecommands.bukkit.context.LocationContext;
import dev.rollczi.litecommands.bukkit.argument.WorldArgument;
import dev.rollczi.litecommands.bukkit.context.WorldContext;
import dev.rollczi.litecommands.bukkit.context.PlayerOnlyContextProvider;
import dev.rollczi.litecommands.bukkit.argument.PlayerArgument;
import dev.rollczi.litecommands.message.MessageRegistry;
import dev.rollczi.litecommands.util.StringUtil;
import java.util.Locale;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public final class LiteCommandsBukkit {

    private LiteCommandsBukkit() {
    }

    public static LiteCommandsBuilder<CommandSender, LiteBukkitSettings, ?> builder() {
        JavaPlugin plugin = JavaPlugin.getProvidingPlugin(LiteCommandsBukkit.class);
        String name = plugin.getDescription().getName();

        return builder(name.toLowerCase(Locale.ROOT).replace(" ", "-"), plugin, plugin.getServer());
    }

    public static LiteCommandsBuilder<CommandSender, LiteBukkitSettings, ?> builder(String fallbackPrefix) {
        return builder(fallbackPrefix, JavaPlugin.getProvidingPlugin(LiteCommandsBukkit.class), Bukkit.getServer());
    }

    public static LiteCommandsBuilder<CommandSender, LiteBukkitSettings, ?> builder(String fallbackPrefix, Plugin plugin) {
        return builder(fallbackPrefix, plugin, Bukkit.getServer());
    }

    public static LiteCommandsBuilder<CommandSender, LiteBukkitSettings, ?> builder(String fallbackPrefix, Plugin plugin, Server server) {
        LiteBukkitSettings settings = new LiteBukkitSettings(server)
            .fallbackPrefix(fallbackPrefix);

        return builder(plugin, server, settings);
    }

    public static LiteCommandsBuilder<CommandSender, LiteBukkitSettings, ?> builder(Plugin plugin, Server server, LiteBukkitSettings settings) {
        return LiteCommandsFactory.builder(CommandSender.class, new BukkitPlatform(settings)).selfProcessor((builder, internal) -> {
            MessageRegistry<CommandSender> messageRegistry = internal.getMessageRegistry();

            builder
                .bind(Server.class, () -> server)
                .bind(BukkitScheduler.class, () -> server.getScheduler())

                .scheduler(new BukkitSchedulerImpl(server.getScheduler(), plugin))

                .settings(bukkitSettings -> bukkitSettings.tabCompleter(TabComplete.create(internal.getScheduler(), plugin)))

                .argument(Player.class, new PlayerArgument(server, messageRegistry))
                .argument(World.class, new WorldArgument(server, messageRegistry))
                .argument(Location.class, new LocationArgument(messageRegistry))

                .context(Player.class, new PlayerOnlyContextProvider(messageRegistry))
                .context(World.class, new WorldContext(messageRegistry))
                .context(Location.class, new LocationContext(messageRegistry))

                .result(String.class, new StringHandler());
        });
    }

}
