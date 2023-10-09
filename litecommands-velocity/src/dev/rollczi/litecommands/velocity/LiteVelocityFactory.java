package dev.rollczi.litecommands.velocity;

import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.rollczi.litecommands.LiteCommandsFactory;
import dev.rollczi.litecommands.adventure.LiteAdventureExtension;
import dev.rollczi.litecommands.builder.LiteCommandsBuilder;

public final class LiteVelocityFactory {

    private LiteVelocityFactory() {
    }

    public static LiteCommandsBuilder<CommandSource, LiteVelocitySettings, ?> builder(ProxyServer proxy) {
        return LiteCommandsFactory.builder(CommandSource.class, new VelocityPlatform(proxy.getCommandManager(), new LiteVelocitySettings()))
            .extension(new LiteAdventureExtension<>())

            .bind(ProxyServer.class, () -> proxy)
            .bind(CommandManager.class, proxy::getCommandManager);
    }

}
