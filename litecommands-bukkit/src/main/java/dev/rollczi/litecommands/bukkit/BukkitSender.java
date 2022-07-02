package dev.rollczi.litecommands.bukkit;

import dev.rollczi.litecommands.platform.LiteSender;
import org.bukkit.command.CommandSender;

class BukkitSender implements LiteSender {

    private final CommandSender handle;

    public BukkitSender(CommandSender handle) {
        this.handle = handle;
    }

    @Override
    public boolean hasPermission(String permission) {
        return this.handle.hasPermission(permission);
    }

    @Override
    public void sendMessage(String message) {
        this.handle.sendMessage(StringHandler.DESERIALIZE_AMPERSAND.apply(message));
    }

    @Override
    public Object getHandle() {
        return this.handle;
    }
}
