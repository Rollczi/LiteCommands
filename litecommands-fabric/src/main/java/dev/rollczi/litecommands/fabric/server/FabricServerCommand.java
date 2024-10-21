package dev.rollczi.litecommands.fabric.server;

import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.fabric.FabricAbstractCommand;
import dev.rollczi.litecommands.platform.PlatformInvocationListener;
import dev.rollczi.litecommands.platform.PlatformSender;
import dev.rollczi.litecommands.platform.PlatformSuggestionListener;
import net.minecraft.server.command.ServerCommandSource;

public class FabricServerCommand extends FabricAbstractCommand<ServerCommandSource> {
    public FabricServerCommand(CommandRoute<ServerCommandSource> baseRoute, PlatformInvocationListener<ServerCommandSource> invocationHook, PlatformSuggestionListener<ServerCommandSource> suggestionHook) {
        super(baseRoute, invocationHook, suggestionHook);
    }

    @Override
    protected PlatformSender createSender(ServerCommandSource serverCommandSource) {
        return new FabricServerSender(serverCommandSource);
    }
}
