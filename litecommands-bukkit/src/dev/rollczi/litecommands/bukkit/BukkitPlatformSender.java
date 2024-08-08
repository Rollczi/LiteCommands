package dev.rollczi.litecommands.bukkit;

import dev.rollczi.litecommands.identifier.Identifier;
import dev.rollczi.litecommands.platform.AbstractPlatformSender;
import dev.rollczi.litecommands.platform.PlatformReceiver;
import org.bukkit.command.CommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Player;

class BukkitPlatformSender extends AbstractPlatformSender implements PlatformReceiver {

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

        if (this.handle instanceof RemoteConsoleCommandSender) {
            RemoteConsoleCommandSender commandSender = (RemoteConsoleCommandSender) this.handle;
            return Identifier.of(RemoteConsoleCommandSender.class, commandSender.getName());
        }

        return Identifier.CONSOLE;
    }

    @Override
    public Comparable<Void> sendMessage(String message) {
        this.handle.sendMessage(message);
        return null;
    }

}
