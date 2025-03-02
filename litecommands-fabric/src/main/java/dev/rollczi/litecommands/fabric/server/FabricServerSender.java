package dev.rollczi.litecommands.fabric.server;

import dev.rollczi.litecommands.identifier.Identifier;
import dev.rollczi.litecommands.platform.AbstractPlatformSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.ServerCommandSource;

import java.util.ArrayList;
import java.util.List;

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
        List<Object> list = new ArrayList<>();
        list.add(source.getName());
        Entity entity = source.getEntity();
        if (entity != null) {
            list.add(entity.getUuid());
        }
        return Identifier.of(list.toArray());
    }

    @Override
    public Object getHandle() {
        return (SENDER) this.source;
    }

}
