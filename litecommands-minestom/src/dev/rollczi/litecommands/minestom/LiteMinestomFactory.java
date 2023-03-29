package dev.rollczi.litecommands.minestom;

import dev.rollczi.litecommands.LiteCommandsFactory;
import dev.rollczi.litecommands.builder.LiteCommandsBuilder;
import net.minestom.server.command.CommandManager;
import net.minestom.server.command.CommandSender;
import net.minestom.server.network.socket.Server;

public final class LiteMinestomFactory {

    private LiteMinestomFactory() {
    }

    public static LiteCommandsBuilder<CommandSender, LiteMinestomSettings, ?> builder(Server server, CommandManager commandManager) {
        return LiteCommandsFactory.builder(CommandSender.class, new MinestomPlatform(commandManager))
            .typeBind(Server.class, () -> server)
            ;
    }

}
