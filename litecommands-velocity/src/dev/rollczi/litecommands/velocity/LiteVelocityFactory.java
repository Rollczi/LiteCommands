package dev.rollczi.litecommands.velocity;

import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.rollczi.litecommands.LiteCommandsFactory;
import dev.rollczi.litecommands.adventure.LiteAdventureExtension;
import dev.rollczi.litecommands.LiteCommandsBuilder;

public final class LiteVelocityFactory {

    private LiteVelocityFactory() {
    }

    @SuppressWarnings("unchecked")
    public static <B extends LiteCommandsBuilder<CommandSource, LiteVelocitySettings, B>> B builder(ProxyServer proxy) {
        return (B) LiteCommandsFactory.builder(CommandSource.class, new VelocityPlatform(proxy.getCommandManager(), new LiteVelocitySettings()))
            .extension(new LiteAdventureExtension<>(), configuration -> configuration
                .legacyColor(true)
            )

            .bind(ProxyServer.class, () -> proxy)
            .bind(CommandManager.class, proxy::getCommandManager);
    }

}
