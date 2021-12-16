package dev.rollczi.litecommands.bungee;

import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.LiteFactory;
import dev.rollczi.litecommands.inject.basic.OriginalSenderBind;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

public final class LiteBungeeFactory {

    public static LiteCommandsBuilder builder(Plugin plugin) {
        return LiteFactory.builder()
                .bind(Plugin.class, plugin)
                .bind(ProxyServer.class, plugin.getProxy())
                .bind(CommandSender.class, new OriginalSenderBind())
                .platform(new LiteBungeePlatformManager(plugin));
    }

}
