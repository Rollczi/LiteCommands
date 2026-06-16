package dev.rollczi.litecommands.fabric.server;

import dev.rollczi.litecommands.identifier.Identifier;
import dev.rollczi.litecommands.platform.AbstractPlatformSender;
import net.minecraft.world.entity.Entity;
import net.minecraft.commands.CommandSourceStack;

public class FabricServerSender extends AbstractPlatformSender {

    private final CommandSourceStack source;

    public FabricServerSender(CommandSourceStack source) {
        this.source = source;
    }

    @Override
    public String getName() {
        return source.getTextName();
    }

    @Override
    public Identifier getIdentifier() {
        Entity entity = source.getEntity();
        if (entity == null) {
            return Identifier.CONSOLE;
        }

        return Identifier.of(source.getTextName(), entity.getUUID());
    }

    @Override
    public Object getHandle() {
        return this.source;
    }

}
