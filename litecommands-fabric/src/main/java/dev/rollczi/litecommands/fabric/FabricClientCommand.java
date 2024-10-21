package dev.rollczi.litecommands.fabric;

import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.platform.PlatformInvocationListener;
import dev.rollczi.litecommands.platform.PlatformSender;
import dev.rollczi.litecommands.platform.PlatformSuggestionListener;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

class FabricClientCommand extends FabricAbstractCommand<FabricClientCommandSource> {
    FabricClientCommand(CommandRoute<FabricClientCommandSource> baseRoute, PlatformInvocationListener<FabricClientCommandSource> invocationHook, PlatformSuggestionListener<FabricClientCommandSource> suggestionHook) {
        super(baseRoute, invocationHook, suggestionHook);
    }

    @Override
    protected PlatformSender createSender(FabricClientCommandSource serverCommandSource) {
        return new FabricClientSender(serverCommandSource);
    }
}
