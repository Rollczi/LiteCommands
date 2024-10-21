package dev.rollczi.litecommands.fabric;

import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.platform.PlatformInvocationListener;
import dev.rollczi.litecommands.platform.PlatformSettings;
import dev.rollczi.litecommands.platform.PlatformSuggestionListener;
import net.minecraft.server.command.ServerCommandSource;

public class FabricServerPlatform extends FabricAbstractPlatform<ServerCommandSource> {
    FabricServerPlatform(PlatformSettings settings) {
        super(settings);
    }

    @Override
    protected void registerEvents() {
        if (COMMAND_API_V2) {
            net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
                for (FabricAbstractCommand<ServerCommandSource> fabricCommand : fabricCommands.values()) {
                    dispatcher.register(fabricCommand.toLiteral());
                }
            });
        } else {
            net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
                for (FabricAbstractCommand<ServerCommandSource> fabricCommand : fabricCommands.values()) {
                    dispatcher.register(fabricCommand.toLiteral());
                }
            });
        }
    }

    @Override
    protected FabricAbstractCommand<ServerCommandSource> createCommand(CommandRoute<ServerCommandSource> commandRoute, PlatformInvocationListener<ServerCommandSource> invocationHook, PlatformSuggestionListener<ServerCommandSource> suggestionHook) {
        return new FabricServerCommand(commandRoute, invocationHook, suggestionHook);
    }
}
