package dev.rollczi.litecommands.sponge;

import dev.rollczi.litecommands.identifier.Identifier;
import dev.rollczi.litecommands.platform.AbstractPlatformSender;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.entity.living.player.Player;

public class SpongeSender extends AbstractPlatformSender {

    private final CommandCause handle;

    public SpongeSender(CommandCause handle) {
        this.handle = handle;
    }

    @Override
    public String getName() {
        if (handle.root() instanceof Player) {
            return ((Player) handle).name();
        }

        return "CONSOLE*";
    }

    @Override
    public Identifier getIdentifier() {
        if (handle.root() instanceof Player) {
            return Identifier.of(((Player) handle).uniqueId());
        }

        return Identifier.CONSOLE;
    }

    @Override
    public Object getHandle() {
        return handle;
    }

}
