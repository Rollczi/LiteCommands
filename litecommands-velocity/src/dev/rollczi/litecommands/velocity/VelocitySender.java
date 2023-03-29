package dev.rollczi.litecommands.velocity;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import dev.rollczi.litecommands.platform.PlatformSender;

class VelocitySender implements PlatformSender {

    private final CommandSource handle;

    public VelocitySender(CommandSource handle) {
        this.handle = handle;
    }

    @Override
    public String getName() {
        return this.handle instanceof Player
            ? ((Player) this.handle).getUsername()
            : "Console";
    }

    @Override
    public boolean hasPermission(String permission) {
        return this.handle.hasPermission(permission);
    }

}
