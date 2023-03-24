package dev.rollczi.litecommands.bungee;

import dev.rollczi.litecommands.platform.PlatformSender;
import net.md_5.bungee.api.CommandSender;

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

}
