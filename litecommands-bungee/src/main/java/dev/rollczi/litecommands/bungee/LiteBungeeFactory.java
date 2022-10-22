package dev.rollczi.litecommands.bungee;

import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.implementation.LiteFactory;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.plugin.Plugin;

public final class LiteBungeeFactory {

    private LiteBungeeFactory() {
    }

    public static LiteCommandsBuilder<CommandSender> builder(Plugin plugin) {
        return LiteFactory.builder(CommandSender.class)
                .typeBind(ProxyServer.class, plugin::getProxy)

                .resultHandler(BaseComponent.class, new BaseComponentHandler())
                .resultHandler(String.class, new StringHandler())

                .platform(new LiteBungeeRegistryPlatform(plugin));
    }

}
