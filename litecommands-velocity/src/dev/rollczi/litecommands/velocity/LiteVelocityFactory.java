package dev.rollczi.litecommands.velocity;

import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import dev.rollczi.litecommands.LiteCommandsFactory;
import dev.rollczi.litecommands.adventure.LiteAdventureExtension;
import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.message.MessageRegistry;
import dev.rollczi.litecommands.velocity.argument.PlayerArgument;
import dev.rollczi.litecommands.velocity.argument.RegisteredServerArgument;
import dev.rollczi.litecommands.velocity.argument.ServerInfoArgument;
import dev.rollczi.litecommands.velocity.context.PlayerOnlyContextProvider;
import dev.rollczi.litecommands.velocity.context.RegisteredServerContextProvider;
import dev.rollczi.litecommands.velocity.context.ServerConnectionContextProvider;
import dev.rollczi.litecommands.velocity.context.ServerInfoContextProvider;

public final class LiteVelocityFactory {

    private LiteVelocityFactory() {
    }

    @SuppressWarnings("unchecked")
    public static <B extends LiteCommandsBuilder<CommandSource, LiteVelocitySettings, B>> B builder(ProxyServer proxy) {
        VelocityPlatform platform = new VelocityPlatform(proxy.getCommandManager(), new LiteVelocitySettings());
        return (B) LiteCommandsFactory.builder(CommandSource.class, platform).selfProcessor((builder, internal) -> {
            MessageRegistry<CommandSource> messages = internal.getMessageRegistry();

            builder
                .extension(new LiteAdventureExtension<>(), configuration -> configuration
                    .legacyColor(true)
                )

                .argument(Player.class, new PlayerArgument(messages, proxy))
                .argument(RegisteredServer.class, new RegisteredServerArgument(messages, proxy))
                .argument(ServerInfo.class, new ServerInfoArgument(messages, proxy))

                .context(ServerConnection.class, new ServerConnectionContextProvider(messages))
                .context(RegisteredServer.class, new RegisteredServerContextProvider(messages))
                .context(ServerInfo.class, new ServerInfoContextProvider(messages))
                .context(Player.class, new PlayerOnlyContextProvider(messages))

                .bind(ProxyServer.class, () -> proxy)
                .bind(CommandManager.class, () -> proxy.getCommandManager());
            }
        );
    }

}
