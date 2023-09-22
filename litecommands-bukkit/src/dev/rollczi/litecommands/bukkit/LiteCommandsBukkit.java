package dev.rollczi.litecommands.bukkit;

import dev.rollczi.litecommands.LiteCommandsFactory;
import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.bukkit.tools.BukkitOnlyPlayerContextual;
import dev.rollczi.litecommands.bukkit.tools.BukkitPlayerArgument;
import org.bukkit.Bukkit;
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

    public static LiteCommandsBuilder<CommandSender, LiteBukkitSettings, ?> builder(Plugin plugin, Server server) {
        return builder(plugin, server, new LiteBukkitSettings(server));
    }

    public static LiteCommandsBuilder<CommandSender, LiteBukkitSettings, ?> builder(Plugin plugin, Server server, LiteBukkitSettings settings) {
        return LiteCommandsFactory.builder(CommandSender.class, new BukkitPlatform(settings))
            .bind(Server.class, () -> server)
            .bind(BukkitScheduler.class, () -> server.getScheduler())
            .scheduler(new BukkitSchedulerImpl(server.getScheduler(), plugin))

            .argument(Player.class, new BukkitPlayerArgument<>(server, name -> "Player " + name + " is not online!")) // TODO WIKI GUIDE links

            .context(Player.class, new BukkitOnlyPlayerContextual<>("This command is only for players!")) //TODO

            .result(String.class, new StringHandler())

            ;
    }

}
