package dev.rollczi.litecommands.fabric.server;

import dev.rollczi.litecommands.identifier.Identifier;
import dev.rollczi.litecommands.platform.AbstractPlatformSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.ServerCommandSource;

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
        Entity entity = source.getEntity();
        if (entity == null) {
            return Identifier.CONSOLE;
        }

        return Identifier.of(source.getName(), entity.getUuid());
    }

    @Override
    public Object getHandle() {
        return this.source;
    }

}
