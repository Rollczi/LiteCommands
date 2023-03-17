package dev.rollczi.litecommands.bungee;

import dev.rollczi.litecommands.modern.platform.PlatformSender;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

class BungeeSender implements PlatformSender {

    private final CommandSender handle;

    public BungeeSender(CommandSender handle) {
        this.handle = handle;
    }

    @Override
    public String getName() {
        return this.handle.getName();
    }

    @Override
    public boolean hasPermission(String permission) {
        return this.handle.hasPermission(permission);
    }

    @Override
    public void sendMessage(String message) {
        this.handle.sendMessage(TextComponent.fromLegacyText(StringHandler.DESERIALIZE_AMPERSAND.apply(message)));
    }

}
