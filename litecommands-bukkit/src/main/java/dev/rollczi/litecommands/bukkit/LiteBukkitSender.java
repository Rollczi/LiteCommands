package dev.rollczi.litecommands.bukkit;

import dev.rollczi.litecommands.platform.LiteSender;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class LiteBukkitSender implements LiteSender {

    private final CommandSender sender;

    public LiteBukkitSender(CommandSender sender) {
        this.sender = sender;
    }

    @Override
    public boolean hasPermission(String permission) {
        return this.sender.hasPermission(permission);
    }

    @Override
    public void sendMessage(String message) {
        this.sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    @Override
    public CommandSender getSender() {
        return this.sender;
    }

}
