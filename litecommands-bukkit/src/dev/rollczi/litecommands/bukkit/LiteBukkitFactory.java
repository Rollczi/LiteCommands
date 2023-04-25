package dev.rollczi.litecommands.bukkit;

import dev.rollczi.litecommands.LiteCommandsFactory;
import dev.rollczi.litecommands.builder.LiteCommandsBuilder;
import dev.rollczi.litecommands.bukkit.tools.BukkitOnlyPlayerContextual;
import dev.rollczi.litecommands.bukkit.tools.BukkitPlayerArgument;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

public final class LiteBukkitFactory {

    private LiteBukkitFactory() {
    }

    public static LiteCommandsBuilder<CommandSender, LiteBukkitSettings, ?> builder(Server server) {
        return builder(server, new LiteBukkitSettings());
    }

    public static LiteCommandsBuilder<CommandSender, LiteBukkitSettings, ?> builder(Server server, LiteBukkitSettings settings) {
        settings.commandsProvider(BukkitCommandsProviderImpl.create(server));

        return LiteCommandsFactory.builder(CommandSender.class, new BukkitPlatform(settings))
            .typeBind(Server.class, () -> server)
            .typeBind(BukkitScheduler.class, server::getScheduler)

            .argumentParser(Player.class, new BukkitPlayerArgument<>(server, name -> "Player " + name + " is not online!")) // TODO WIKI GUIDE links
            .contextualBind(Player.class, new BukkitOnlyPlayerContextual<>("This command is only for players!")) //TODO
            .resultHandler(String.class, new StringHandler())
            ;
    }

}
