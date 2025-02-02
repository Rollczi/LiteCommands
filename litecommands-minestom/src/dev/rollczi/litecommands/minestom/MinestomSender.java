package dev.rollczi.litecommands.minestom;

import dev.rollczi.litecommands.identifier.Identifier;
import dev.rollczi.litecommands.platform.AbstractPlatformSender;
import net.kyori.adventure.pointer.Pointer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.command.ServerSender;
import net.minestom.server.entity.Player;

class MinestomSender extends AbstractPlatformSender {

    private final CommandSender handle;
    private final MinestomPlatform platform;

    public MinestomSender(CommandSender handle, MinestomPlatform platform) {
        this.handle = handle;
        this.platform = platform;
    }

    @Override
    public boolean hasPermission(String permission) {
        return this.platform.getConfiguration().hasPermission(this.handle, permission);
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

}
