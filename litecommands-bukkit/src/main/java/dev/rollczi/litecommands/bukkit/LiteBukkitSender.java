package dev.rollczi.litecommands.bukkit;

import dev.rollczi.litecommands.LiteSender;
import org.bukkit.command.CommandSender;

public class LiteBukkitSender implements LiteSender {

    private final CommandSender sender;

    public LiteBukkitSender(CommandSender sender) {
        this.sender = sender;
    }

    public boolean hasPermission(String permission) {
        return sender.hasPermission(permission);
    }

    public void sendMessage(String message) {
        sender.sendMessage(message);
    }

    public CommandSender getSender() {
        return sender;
    }

}
