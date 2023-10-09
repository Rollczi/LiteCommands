package dev.rollczi.litecommands.bukkit;

import dev.rollczi.litecommands.identifier.Identifier;
import dev.rollczi.litecommands.platform.AbstractPlatformSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class BukkitPlatformSender extends AbstractPlatformSender {

    private final CommandSender handle;

    public BukkitPlatformSender(CommandSender handle) {
        this.handle = handle;
    }

    @Override
    public boolean hasPermission(String permission) {
        return this.handle.hasPermission(permission);
    }

    @Override
    public String getName() {
        return this.handle.getName();
    }

    @Override
    public String getDisplayName() {
        if (this.handle instanceof Player) {
            return ((Player) this.handle).getDisplayName();
        }

        return this.handle.getName();
    }

    @Override
    public Identifier getIdentifier() {
        if (this.handle instanceof Player) {
            return Identifier.of(((Player) this.handle).getUniqueId());
        }

        return Identifier.CONSOLE;
    }

}
