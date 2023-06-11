package dev.rollczi.litecommands.bukkit;

import dev.rollczi.litecommands.LiteCommandsFactory;
import dev.rollczi.litecommands.builder.LiteCommandsBuilder;
import dev.rollczi.litecommands.bukkit.tools.BukkitOnlyPlayerContextual;
import dev.rollczi.litecommands.bukkit.tools.BukkitPlayerArgument;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

public final class LiteCommandsBukkit {

    private LiteCommandsBukkit() {
    }

    public static LiteCommandsBuilder<CommandSender, LiteBukkitSettings, ?> builder(Server server) {
        return builder(server, new LiteBukkitSettings(server));
    }

    public static LiteCommandsBuilder<CommandSender, LiteBukkitSettings, ?> builder(Server server, LiteBukkitSettings settings) {
        return LiteCommandsFactory.builder(CommandSender.class, new BukkitPlatform(settings))
            .bind(Server.class, () -> server)
            .bind(BukkitScheduler.class, server::getScheduler)

            .argument(Player.class, new BukkitPlayerArgument<>(server, name -> "Player " + name + " is not online!")) // TODO WIKI GUIDE links

            .context(Player.class, new BukkitOnlyPlayerContextual<>("This command is only for players!")) //TODO

            .result(String.class, new StringHandler())

            ;
    }

}
