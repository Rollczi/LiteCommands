package dev.rollczi.litecommands.minestom;

import dev.rollczi.litecommands.platform.LiteSender;
import net.minestom.server.command.CommandSender;

class MinestomSender implements LiteSender {

    private final CommandSender handle;

    public MinestomSender(CommandSender handle) {
        this.handle = handle;
    }

    @Override
    public boolean hasPermission(String permission) {
        return this.handle.hasPermission(permission);
    }

    @Override
    public void sendMessage(String message) {
        this.handle.sendMessage(StringHandler.MINI_MESSAGE.deserialize(message));
    }

    @Override
    public Object getHandle() {
        return this.handle;
    }
}
