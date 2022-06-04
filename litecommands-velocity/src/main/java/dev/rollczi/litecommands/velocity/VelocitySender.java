package dev.rollczi.litecommands.velocity;

import com.velocitypowered.api.command.CommandSource;
import dev.rollczi.litecommands.platform.LiteSender;

class VelocitySender implements LiteSender {

    private final CommandSource handle;

    public VelocitySender(CommandSource handle) {
        this.handle = handle;
    }

    @Override
    public boolean hasPermission(String permission) {
        return this.handle.hasPermission(permission);
    }

    @Override
    public Object getHandle() {
        return this.handle;
    }

}
