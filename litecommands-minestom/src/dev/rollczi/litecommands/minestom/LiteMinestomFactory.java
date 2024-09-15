package dev.rollczi.litecommands.minestom;

import dev.rollczi.litecommands.LiteCommandsFactory;
import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.adventure.LiteAdventureExtension;
import dev.rollczi.litecommands.message.MessageRegistry;
import dev.rollczi.litecommands.minestom.argument.InstanceArgument;
import dev.rollczi.litecommands.minestom.argument.PlayerArgument;
import dev.rollczi.litecommands.minestom.context.InstanceContext;
import dev.rollczi.litecommands.minestom.context.PlayerContext;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandManager;
import net.minestom.server.command.CommandSender;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.network.ConnectionManager;
import net.minestom.server.network.socket.Server;

public final class LiteMinestomFactory {

    private LiteMinestomFactory() {
    }

    public static <B extends LiteCommandsBuilder<CommandSender, LiteMinestomSettings, B>> B builder() {
        return builder(MinecraftServer.getServer(), MinecraftServer.getInstanceManager(), MinecraftServer.getConnectionManager(), MinecraftServer.getCommandManager());
    }

    @SuppressWarnings("unchecked")
    public static <B extends LiteCommandsBuilder<CommandSender, LiteMinestomSettings, B>> B builder(
        Server server,
        InstanceManager instanceManager,
        ConnectionManager connectionManager,
        CommandManager commandManager
    ) {
        return (B) LiteCommandsFactory.builder(CommandSender.class, new MinestomPlatform(commandManager)).self((builder, internal) -> {
            MessageRegistry<CommandSender> messageRegistry = internal.getMessageRegistry();

            builder
                .extension(new LiteAdventureExtension<>(), configuration -> configuration
                    .legacyColor(true)
                )
                .argument(Player.class, new PlayerArgument(connectionManager, messageRegistry))
                .argument(Instance.class, new InstanceArgument(instanceManager, messageRegistry))
                .context(Player.class, new PlayerContext(messageRegistry))
                .context(Instance.class, new InstanceContext(messageRegistry))
                .bind(Server.class, () -> server);
        });
    }

}
