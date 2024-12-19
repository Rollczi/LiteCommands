package dev.rollczi.litecommands.fabric.server;

import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.fabric.FabricAbstractCommand;
import dev.rollczi.litecommands.platform.PlatformInvocationListener;
import dev.rollczi.litecommands.platform.PlatformSuggestionListener;
import net.minecraft.server.command.ServerCommandSource;

public class FabricServerCommand extends FabricAbstractCommand<ServerCommandSource> {
    public FabricServerCommand(FabricServerPlatform platform, CommandRoute<ServerCommandSource> baseRoute, PlatformInvocationListener<ServerCommandSource> invocationHook, PlatformSuggestionListener<ServerCommandSource> suggestionHook) {
        super(platform, baseRoute, invocationHook, suggestionHook);
    }
}
