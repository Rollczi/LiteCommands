package dev.rollczi.litecommands.velocity;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import dev.rollczi.litecommands.identifier.Identifier;
import dev.rollczi.litecommands.platform.AbstractPlatformSender;

class VelocitySender extends AbstractPlatformSender {

    private final CommandSource handle;

    public VelocitySender(CommandSource handle) {
        this.handle = handle;
    }

    @Override
    public String getName() {
        return this.handle instanceof Player
            ? ((Player) this.handle).getUsername()
            : "CONSOLE*";
    }

    @Override
    public Identifier getIdentifier() {
        if (this.handle instanceof Player) {
            return Identifier.of(((Player) this.handle).getUniqueId());
        }

        return Identifier.CONSOLE;
    }

    @Override
    public Object getHandle() {
        return this.handle;
    }

}
