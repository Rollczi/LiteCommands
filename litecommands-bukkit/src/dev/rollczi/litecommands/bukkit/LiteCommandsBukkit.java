package dev.rollczi.litecommands.bukkit;

import dev.rollczi.litecommands.LiteCommandsFactory;
import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.bukkit.argument.LocationArgument;
import dev.rollczi.litecommands.bukkit.context.PlayerOnlyContextProvider;
import dev.rollczi.litecommands.bukkit.argument.PlayerArgument;
import dev.rollczi.litecommands.message.MessageRegistry;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public final class LiteCommandsBukkit {

    private LiteCommandsBukkit() {
    }

    public static LiteCommandsBuilder<CommandSender, LiteBukkitSettings, ?> builder() {
        return builder(JavaPlugin.getProvidingPlugin(LiteCommandsBukkit.class), Bukkit.getServer());
    }

    public static LiteCommandsBuilder<CommandSender, LiteBukkitSettings, ?> builder(Plugin plugin) {
        return builder(plugin, Bukkit.getServer());
    }

    public static LiteCommandsBuilder<CommandSender, LiteBukkitSettings, ?> builder(Plugin plugin, Server server) {
        return builder(plugin, server, new LiteBukkitSettings(server));
    }

    public static LiteCommandsBuilder<CommandSender, LiteBukkitSettings, ?> builder(Plugin plugin, Server server, LiteBukkitSettings settings) {
        return LiteCommandsFactory.builder(CommandSender.class, new BukkitPlatform(settings)).selfProcessor((builder, pattern) -> {
            MessageRegistry messageRegistry = pattern.getMessageRegistry();

            builder
                .bind(Server.class, () -> server)
                .bind(BukkitScheduler.class, () -> server.getScheduler())

                .scheduler(new BukkitSchedulerImpl(server.getScheduler(), plugin))

                .argument(Location.class, new LocationArgument(messageRegistry))
                .argument(Player.class, new PlayerArgument(server, messageRegistry))

                .context(Player.class, new PlayerOnlyContextProvider(messageRegistry))

                .result(String.class, new StringHandler());
        });
    }

}
