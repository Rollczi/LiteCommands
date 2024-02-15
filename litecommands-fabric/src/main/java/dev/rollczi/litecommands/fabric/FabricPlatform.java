package dev.rollczi.litecommands.fabric;

import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.platform.AbstractPlatform;
import dev.rollczi.litecommands.platform.Platform;
import dev.rollczi.litecommands.platform.PlatformInvocationListener;
import dev.rollczi.litecommands.platform.PlatformSettings;
import dev.rollczi.litecommands.platform.PlatformSuggestionListener;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

class FabricPlatform extends AbstractPlatform<ServerCommandSource, PlatformSettings> implements Platform<ServerCommandSource, PlatformSettings> {

    private final Map<UUID, FabricCommand> fabricCommands = new HashMap<>();

    FabricPlatform(PlatformSettings settings) {
        super(settings);
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            for (FabricCommand fabricCommand : fabricCommands.values()) {
                dispatcher.register(fabricCommand.toLiteral());
            }
        });
    }

    @Override
    protected void hook(CommandRoute<ServerCommandSource> commandRoute, PlatformInvocationListener<ServerCommandSource> invocationHook, PlatformSuggestionListener<ServerCommandSource> suggestionHook) {
        fabricCommands.put(commandRoute.getUniqueId(), new FabricCommand(commandRoute, invocationHook, suggestionHook));
    }

    @Override
    protected void unhook(CommandRoute<ServerCommandSource> commandRoute) {
        fabricCommands.remove(commandRoute.getUniqueId());
        // TODO: unregister command from dispatcher
    }

}
