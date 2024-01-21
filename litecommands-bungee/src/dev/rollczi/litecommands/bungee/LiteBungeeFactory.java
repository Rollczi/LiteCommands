package dev.rollczi.litecommands.bungee;

import dev.rollczi.litecommands.LiteCommandsFactory;
import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.bungee.tools.BungeeOnlyPlayerContextual;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

public final class LiteBungeeFactory {

    private LiteBungeeFactory() {
    }

    public static <B extends LiteCommandsBuilder<CommandSender, LiteBungeeSettings, B>> B builder(Plugin plugin) {
        return builder(plugin, new LiteBungeeSettings());
    }

    @SuppressWarnings("unchecked")
    public static <B extends LiteCommandsBuilder<CommandSender, LiteBungeeSettings, B>> B builder(Plugin plugin, LiteBungeeSettings liteBungeeSettings) {
        return (B) LiteCommandsFactory.builder(CommandSender.class, new BungeePlatform(plugin, liteBungeeSettings))
            .bind(ProxyServer.class, plugin::getProxy)
            .context(ProxiedPlayer.class, new BungeeOnlyPlayerContextual<>("Only players can use this command! (Set this message in LiteBungeeFactory)"))

            .result(BaseComponent.class, new BaseComponentHandler())
            .result(String.class, new StringHandler());
    }

}
