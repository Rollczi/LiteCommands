package dev.rollczi.litecommands.velocity;

import com.velocitypowered.api.command.CommandSource;
import dev.rollczi.litecommands.LiteSender;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class LiteVelocitySender implements LiteSender {

    private final CommandSource source;

    public LiteVelocitySender(CommandSource source) {
        this.source = source;
    }

    @Override
    public boolean hasPermission(String permission) {
        return source.hasPermission(permission);
    }

    @Override
    public void sendMessage(String message) {
        source.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(message));
    }

    @Override
    public CommandSource getSender() {
        return source;
    }

}
