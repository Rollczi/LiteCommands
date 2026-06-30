package dev.rollczi.litecommands.fabric.server;

import dev.rollczi.litecommands.fabric.FabricAbstractPlatform;
import dev.rollczi.litecommands.fabric.LiteFabricSettings;
import dev.rollczi.litecommands.permission.PermissionService;
import dev.rollczi.litecommands.platform.PlatformSender;
import dev.rollczi.litecommands.platform.PlatformSenderFactory;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;

public class FabricServerPlatform extends FabricAbstractPlatform<CommandSourceStack> {

    public FabricServerPlatform(LiteFabricSettings settings, PermissionService permissionService) {
        super(settings, permissionService);
    }

    @Override
    protected void registerEvents() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            this.registerAllCommands(dispatcher);
        });
    }

    @Override
    public PlatformSenderFactory<CommandSourceStack> getSenderFactory() {
        return nativeSender -> createSender(nativeSender);
    }

    @Override
    public PlatformSender createSender(CommandSourceStack nativeSender) {
        return new FabricServerSender(nativeSender);
    }
}
