package dev.rollczi.litecommands.fabric;

import dev.rollczi.litecommands.identifier.Identifier;
import dev.rollczi.litecommands.platform.AbstractPlatformSender;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public class FabricClientSender extends AbstractPlatformSender {
    private final FabricClientCommandSource source;

    public FabricClientSender(FabricClientCommandSource source) {
        this.source = source;
    }

    @Override
    public String getName() {
        return source.getPlayer().getGameProfile().getName();
    }

    @Override
    public Identifier getIdentifier() {
        return Identifier.of(source.getEntity().getUuid());
    }

    @Override
    public boolean hasPermission(String permission) {
        return false;
    }
}
