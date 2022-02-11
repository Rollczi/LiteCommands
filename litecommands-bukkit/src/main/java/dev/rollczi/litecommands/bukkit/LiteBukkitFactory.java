package dev.rollczi.litecommands.bukkit;

import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.LiteFactory;
import dev.rollczi.litecommands.bind.basic.OriginalSenderBind;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;

public final class LiteBukkitFactory {

    private LiteBukkitFactory() {

    }

    public static LiteCommandsBuilder<CommandSender, LiteBukkitPlatformManager> builder(Server server, String fallbackPrefix) {
        return LiteFactory.<CommandSender, LiteBukkitPlatformManager>builder()
                .bind(Server.class, server)
                .bind(CommandSender.class, new OriginalSenderBind())
                .platform(new LiteBukkitPlatformManager(server, fallbackPrefix));
    }

}
