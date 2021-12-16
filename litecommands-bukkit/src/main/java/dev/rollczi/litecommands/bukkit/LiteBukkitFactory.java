package dev.rollczi.litecommands.bukkit;

import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.LiteFactory;
import dev.rollczi.litecommands.inject.basic.OriginalSenderBind;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;

public final class LiteBukkitFactory {

    private LiteBukkitFactory() {

    }

    public static LiteCommandsBuilder builder(Server server, String fallbackPrefix) {
        return LiteFactory.builder()
                .bind(Server.class, server)
                .bind(CommandSender.class, new OriginalSenderBind())
                .platform(new LiteBukkitPlatformManager(server, fallbackPrefix));
    }

}
