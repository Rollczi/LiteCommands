package dev.rollczi.litecommands.minestom;

import dev.rollczi.litecommands.identifier.Identifier;
import dev.rollczi.litecommands.minestom.settings.PermissionResolver;
import dev.rollczi.litecommands.platform.AbstractPlatformSender;
import dev.rollczi.litecommands.platform.PlatformSender;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.command.ServerSender;
import net.minestom.server.entity.Player;

import java.util.function.BiFunction;

class MinestomSender extends AbstractPlatformSender {

    private final CommandSender handle;
    private MinestomPlatform platform;

    public MinestomSender(CommandSender handle) {
        this.handle = handle;
    }

    public void setPlatform(MinestomPlatform platform) {
        // We can't pass the platform directly into the constructor,
        // because the sender factory isn't able to access the platform instance
        // in the super constructor call of the platform itself.
        this.platform = platform;
    }

    @Override
    public boolean hasPermission(String permission) {
        if(this.platform == null) {
            throw new NullPointerException("MinestomPlatform not set");
        }
        PermissionResolver permissionResolver = this.platform.getConfiguration().getPermissionResolver();
        if(permission == null) {
            return true;
        }
        return permissionResolver.hasPermission(this.handle, permission);
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
