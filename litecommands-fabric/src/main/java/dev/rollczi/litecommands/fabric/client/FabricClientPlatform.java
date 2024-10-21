package dev.rollczi.litecommands.fabric.client;

import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.fabric.FabricAbstractCommand;
import dev.rollczi.litecommands.fabric.FabricAbstractPlatform;
import dev.rollczi.litecommands.platform.PlatformInvocationListener;
import dev.rollczi.litecommands.platform.PlatformSettings;
import dev.rollczi.litecommands.platform.PlatformSuggestionListener;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

@Environment(EnvType.CLIENT)
public class FabricClientPlatform extends FabricAbstractPlatform<FabricClientCommandSource> {

    public FabricClientPlatform(PlatformSettings settings) {
        super(settings);
    }

    @Override
    protected void registerEvents() {
        if (!COMMAND_API_V2) {
            throw new UnsupportedOperationException("The current 'fabric-api' does not include fabric-command-api-v2. Please update 'fabric-api'");
        }
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, commandRegistryAccess) -> {
            for (FabricAbstractCommand<FabricClientCommandSource> fabricCommand : fabricCommands.values()) {
                dispatcher.register(fabricCommand.toLiteral());
            }
        });
    }

    @Override
    protected FabricAbstractCommand<FabricClientCommandSource> createCommand(CommandRoute<FabricClientCommandSource> commandRoute, PlatformInvocationListener<FabricClientCommandSource> invocationHook, PlatformSuggestionListener<FabricClientCommandSource> suggestionHook) {
        return new FabricClientCommand(commandRoute, invocationHook, suggestionHook);
    }
}
