package dev.rollczi.litecommands.fabric.server;

import dev.rollczi.litecommands.identifier.Identifier;
import dev.rollczi.litecommands.platform.AbstractPlatformSender;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.command.ServerCommandSource;

@Environment(EnvType.SERVER)
public class FabricServerSender extends AbstractPlatformSender {
    private final ServerCommandSource source;

    public FabricServerSender(ServerCommandSource source) {
        this.source = source;
    }

    @Override
    public String getName() {
        return source.getName();
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
