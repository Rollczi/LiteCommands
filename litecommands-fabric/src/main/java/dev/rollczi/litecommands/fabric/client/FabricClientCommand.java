package dev.rollczi.litecommands.fabric.client;

import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.fabric.FabricAbstractCommand;
import dev.rollczi.litecommands.platform.PlatformInvocationListener;
import dev.rollczi.litecommands.platform.PlatformSuggestionListener;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

@Environment(EnvType.CLIENT)
public class FabricClientCommand extends FabricAbstractCommand<FabricClientCommandSource> {
    public FabricClientCommand(FabricClientPlatform platform, CommandRoute<FabricClientCommandSource> baseRoute, PlatformInvocationListener<FabricClientCommandSource> invocationHook, PlatformSuggestionListener<FabricClientCommandSource> suggestionHook) {
        super(platform, baseRoute, invocationHook, suggestionHook);
    }
}
