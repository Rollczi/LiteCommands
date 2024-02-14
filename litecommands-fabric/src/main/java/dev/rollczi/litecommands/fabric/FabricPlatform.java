package dev.rollczi.litecommands.fabric;

import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.platform.AbstractPlatform;
import dev.rollczi.litecommands.platform.Platform;
import dev.rollczi.litecommands.platform.PlatformInvocationListener;
import dev.rollczi.litecommands.platform.PlatformSettings;
import dev.rollczi.litecommands.platform.PlatformSuggestionListener;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class FabricPlatform extends AbstractPlatform<ServerCommandSource, PlatformSettings> implements Platform<ServerCommandSource, PlatformSettings> {
    private final List<FabricCommand> fabricCommands = new ArrayList<>();

    FabricPlatform(PlatformSettings settings) {
        super(settings);
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            for (FabricCommand instance : fabricCommands) {
                instance.register(dispatcher);
            }
        });
    }

    @Override
    protected void hook(CommandRoute<ServerCommandSource> commandRoute, PlatformInvocationListener<ServerCommandSource> invocationHook, PlatformSuggestionListener<ServerCommandSource> suggestionHook) {
        fabricCommands.add(new FabricCommand(commandRoute, invocationHook, suggestionHook));
    }

    @Override
    protected void unhook(CommandRoute<ServerCommandSource> commandRoute) {
        fabricCommands.removeIf(instance -> Objects.equals(instance.routeUUID(), commandRoute.getUniqueId()));
    }

}
