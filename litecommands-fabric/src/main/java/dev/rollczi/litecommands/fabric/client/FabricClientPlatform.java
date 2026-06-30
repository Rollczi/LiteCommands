package dev.rollczi.litecommands.fabric.client;

import dev.rollczi.litecommands.fabric.FabricAbstractPlatform;
import dev.rollczi.litecommands.fabric.LiteFabricSettings;
import dev.rollczi.litecommands.permission.PermissionService;
import dev.rollczi.litecommands.platform.PlatformSender;
import dev.rollczi.litecommands.platform.PlatformSenderFactory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

@Environment(EnvType.CLIENT)
public class FabricClientPlatform extends FabricAbstractPlatform<FabricClientCommandSource> {

    public FabricClientPlatform(LiteFabricSettings settings, PermissionService permissionService) {
        super(settings, permissionService);
    }

    @Override
    protected void registerEvents() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, commandRegistryAccess) -> {
            this.registerAllCommands(dispatcher);
        });
    }

    @Override
    public PlatformSenderFactory<FabricClientCommandSource> getSenderFactory() {
        return nativeSender -> createSender(nativeSender);
    }

    @Override
    public PlatformSender createSender(FabricClientCommandSource nativeSender) {
        return new FabricClientSender(nativeSender);
    }
}
