package dev.rollczi.litecommands.sponge.internal;

import dev.rollczi.litecommands.identifier.Identifier;
import dev.rollczi.litecommands.platform.AbstractPlatformSender;
import org.spongepowered.api.command.CommandCause;

public class LiteSpongeSender extends AbstractPlatformSender {

    private final CommandCause commandCause;

    public LiteSpongeSender(CommandCause commandCause) {
        this.commandCause = commandCause;
    }

    @Override
    public String getName() {
        return commandCause.identifier();
    }

    @Override
    public Identifier getIdentifier() {
        return Identifier.of(commandCause.identifier());
    }

    @Override
    public boolean hasPermission(String permission) {
        return false;
    }
}
