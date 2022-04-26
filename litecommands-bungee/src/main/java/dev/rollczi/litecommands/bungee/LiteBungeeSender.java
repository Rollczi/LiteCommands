package dev.rollczi.litecommands.bungee;

import dev.rollczi.litecommands.platform.LiteSender;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

public class LiteBungeeSender implements LiteSender {

    private final CommandSender sender;

    public LiteBungeeSender(CommandSender sender) {
        this.sender = sender;
    }

    @Override
    public boolean hasPermission(String permission) {
        return this.sender.hasPermission(permission);
    }

    @Override
    public void sendMessage(String message) {
        this.sender.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', message)));
    }

    @Override
    public CommandSender getSender() {
        return this.sender;
    }

}
