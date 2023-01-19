package dev.rollczi.litecommands.minestom;

import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.implementation.LiteFactory;
import net.minestom.server.command.CommandSender;
import net.minestom.server.network.socket.Server;

public final class LiteMinestomFactory {

    private LiteMinestomFactory() {
    }

    public static LiteCommandsBuilder<CommandSender> builder(Server server) {
        return builder(server, false);
    }


    public static LiteCommandsBuilder<CommandSender> builder(Server server, boolean nativePermissions) {
        LiteMinestomRegistryPlatform registryPlatform = new LiteMinestomRegistryPlatform(server, nativePermissions);

        return LiteFactory.builder(CommandSender.class)
            .typeBind(Server.class, () -> server)

            .resultHandler(String.class, new StringHandler())

            .platform(registryPlatform)
            .afterRegister((builder, platform, injector, executeResultHandler, commandService) -> registryPlatform.setExecuteResultHandler(executeResultHandler));
    }

}
