package dev.rollczi.litecommands.minestom;

import dev.rollczi.litecommands.identifier.Identifier;
import dev.rollczi.litecommands.platform.AbstractPlatformSender;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.command.ServerSender;
import net.minestom.server.entity.Player;

class MinestomSender extends AbstractPlatformSender {

    private final CommandSender handle;

    public MinestomSender(CommandSender handle) {
        this.handle = handle;
    }

    @Override
    public String getName() {
        if (this.handle instanceof ConsoleSender) {
            return "CONSOLE";
        }

        if (this.handle instanceof ServerSender) {
            return "SERVER*";
        }

        if (this.handle instanceof Player player) {
            return player.getUsername();
        }

        throw new IllegalStateException("Unknown sender type: " + this.handle.getClass().getName());
    }

    @Override
    public Identifier getIdentifier() {
        if (this.handle instanceof Player player) {
            return Identifier.of(player.getUuid());
        }

        return Identifier.CONSOLE;
    }

    @Override
    public Object getHandle() {
        return this.handle;
    }

}
