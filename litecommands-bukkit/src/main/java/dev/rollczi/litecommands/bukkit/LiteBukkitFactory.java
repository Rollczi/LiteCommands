package dev.rollczi.litecommands.bukkit;

import dev.rollczi.litecommands.bukkit.tools.BukkitOnlyPlayerContextual;
import dev.rollczi.litecommands.bukkit.tools.BukkitPlayerArgument;
import dev.rollczi.litecommands.modern.LiteCommandsBuilder;
import dev.rollczi.litecommands.modern.LiteCommandsFactory;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

public final class LiteBukkitFactory {

    private LiteBukkitFactory() {
    }

    public static LiteCommandsBuilder<CommandSender, LiteBukkitConfiguration, ?> builder(Server server) {
        BukkitPlatform registryPlatform = new BukkitPlatform(server, new LiteBukkitConfiguration());

        return LiteCommandsFactory.builder(CommandSender.class, registryPlatform.getConfiguration())
            .platform(registryPlatform)

            .typeBind(Server.class, () -> server)
            .typeBind(BukkitScheduler.class, server::getScheduler)

            .argument(Player.class, new BukkitPlayerArgument<>(server, name -> "Player " + name + " is not online!")) // TODO WIKI GUIDE links
            .contextualBind(Player.class, new BukkitOnlyPlayerContextual<>("This command is only for players!")) //TODO
            .resultHandler(String.class, new StringHandler())
            ;
    }

}
