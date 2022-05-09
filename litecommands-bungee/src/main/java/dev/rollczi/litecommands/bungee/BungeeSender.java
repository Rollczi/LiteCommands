package dev.rollczi.litecommands.bungee;

import dev.rollczi.litecommands.platform.LiteSender;
import net.md_5.bungee.api.CommandSender;

class BungeeSender implements LiteSender {

    private final CommandSender sender;

    public BungeeSender(CommandSender sender) {
        this.sender = sender;
    }

    @Override
    public boolean hasPermission(String permission) {
        return this.sender.hasPermission(permission);
    }

    @Override
    public Object getHandle() {
        return this.sender;
    }

}
