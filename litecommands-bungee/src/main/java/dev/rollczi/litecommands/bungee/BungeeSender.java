package dev.rollczi.litecommands.bungee;

import dev.rollczi.litecommands.platform.LiteSender;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

class BungeeSender implements LiteSender {

    private final CommandSender handle;

    public BungeeSender(CommandSender handle) {
        this.handle = handle;
    }

    @Override
    public boolean hasPermission(String permission) {
        return this.handle.hasPermission(permission);
    }

    @Override
    public void sendMessage(String message) {
        this.handle.sendMessage(TextComponent.fromLegacyText(StringHandler.DESERIALIZE_AMPERSAND.apply(message)));
    }

    @Override
    public Object getHandle() {
        return this.handle;
    }

}
