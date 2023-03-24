package dev.rollczi.litecommands.bukkit;

import dev.rollczi.litecommands.platform.PlatformSender;
import org.bukkit.command.CommandSender;

class BukkitSender implements PlatformSender {

    private final CommandSender handle;

    public BukkitSender(CommandSender handle) {
        this.handle = handle;
    }

    @Override
    public String getName() {
        return handle.getName();
    }

    @Override
    public boolean hasPermission(String permission) {
        return this.handle.hasPermission(permission);
    }

}
