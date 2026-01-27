package dev.rollczi.litecommands.hytale;

import dev.rollczi.litecommands.hytale.stubs.HytalePlayer;
import dev.rollczi.litecommands.hytale.stubs.HytaleSource;
import dev.rollczi.litecommands.identifier.Identifier;
import dev.rollczi.litecommands.platform.AbstractPlatformSender;

class HytaleSender extends AbstractPlatformSender {

    private final HytaleSource handle;

    public HytaleSender(HytaleSource handle) {
        this.handle = handle;
    }

    @Override
    public String getName() {
        return this.handle instanceof HytalePlayer
            ? this.handle.getName()
            : "CONSOLE*";
    }

    @Override
    public Identifier getIdentifier() {
        if (this.handle instanceof HytalePlayer) {
            return Identifier.of(this.handle.getId());
        }
        return Identifier.CONSOLE;
    }

    @Override
    public Object getHandle() {
        return this.handle;
    }

}
