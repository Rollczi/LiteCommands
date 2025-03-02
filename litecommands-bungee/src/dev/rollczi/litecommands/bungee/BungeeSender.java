package dev.rollczi.litecommands.bungee;

import dev.rollczi.litecommands.identifier.Identifier;
import dev.rollczi.litecommands.platform.AbstractPlatformSender;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

class BungeeSender extends AbstractPlatformSender {

    private final CommandSender handle;

    public BungeeSender(CommandSender handle) {
        this.handle = handle;
    }

    @Override
    public String getName() {
        return this.handle.getName();
    }

    @Override
    public String getDisplayName() {
        if (this.handle instanceof ProxiedPlayer) {
            return ((ProxiedPlayer) this.handle).getDisplayName();
        }

        return this.handle.getName();
    }

    @Override
    public Identifier getIdentifier() {
        if (this.handle instanceof ProxiedPlayer) {
            return Identifier.of(((ProxiedPlayer) this.handle).getUniqueId());
        }

        return Identifier.CONSOLE;
    }

    @Override
    public Object getHandle() {
        return this.handle;
    }

}
