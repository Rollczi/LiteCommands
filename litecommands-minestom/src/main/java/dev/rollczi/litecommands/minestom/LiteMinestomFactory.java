package dev.rollczi.litecommands.minestom;

import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.implementation.LiteFactory;
import net.minestom.server.command.CommandManager;
import net.minestom.server.command.CommandSender;
import net.minestom.server.network.socket.Server;

public final class LiteMinestomFactory {

    private LiteMinestomFactory() {
    }

    public static LiteCommandsBuilder<CommandSender> builder(Server server, CommandManager commandManager) {
        LiteMinestomRegistryPlatform registryPlatform = new LiteMinestomRegistryPlatform(commandManager);

        return LiteFactory.builder(CommandSender.class)
            .typeBind(Server.class, () -> server)

            .resultHandler(String.class, new StringHandler())

            .platform(registryPlatform);
    }

}
