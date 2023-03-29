package dev.rollczi.litecommands.minestom;

import dev.rollczi.litecommands.platform.PlatformSender;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.entity.Player;

class MinestomSender implements PlatformSender {

    private final CommandSender handle;

    public MinestomSender(CommandSender handle) {
        this.handle = handle;
    }

    @Override
    public String getName() {
        if (this.handle instanceof ConsoleSender) {
            return "CONSOLE";
        }

        if (this.handle instanceof Player player) {
            return player.getUsername();
        }

        throw new IllegalStateException("Unknown sender type: " + this.handle.getClass().getName());
    }

    @Override
    public boolean hasPermission(String permission) {
        return this.handle.hasPermission(permission);
    }

}
