package dev.rollczi.litecommands.fabric;

import dev.rollczi.litecommands.identifier.Identifier;
import dev.rollczi.litecommands.platform.AbstractPlatformSender;
import net.minecraft.server.command.ServerCommandSource;

public class FabricSender extends AbstractPlatformSender {
    private final ServerCommandSource source;

    public FabricSender(ServerCommandSource source) {
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
