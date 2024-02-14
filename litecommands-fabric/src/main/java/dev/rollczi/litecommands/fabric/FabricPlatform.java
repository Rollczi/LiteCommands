package dev.rollczi.litecommands.fabric;

import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.platform.AbstractPlatform;
import dev.rollczi.litecommands.platform.Platform;
import dev.rollczi.litecommands.platform.PlatformInvocationListener;
import dev.rollczi.litecommands.platform.PlatformSettings;
import dev.rollczi.litecommands.platform.PlatformSuggestionListener;
import net.minecraft.server.command.ServerCommandSource;

import java.util.Objects;

class FabricPlatform extends AbstractPlatform<ServerCommandSource, PlatformSettings> implements Platform<ServerCommandSource, PlatformSettings> {
    FabricPlatform(PlatformSettings settings) {
        super(settings);
    }

    @Override
    protected void hook(CommandRoute<ServerCommandSource> commandRoute, PlatformInvocationListener<ServerCommandSource> invocationHook, PlatformSuggestionListener<ServerCommandSource> suggestionHook) {
        new FabricCommand(commandRoute, invocationHook, suggestionHook);
    }

    @Override
    protected void unhook(CommandRoute<ServerCommandSource> commandRoute) {
        FabricCommand.INSTANCES.removeIf(instance -> Objects.equals(instance.routeUUID(), commandRoute.getUniqueId()));
    }

}
