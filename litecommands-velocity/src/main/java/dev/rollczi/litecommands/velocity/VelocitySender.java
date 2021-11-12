package dev.rollczi.litecommands.velocity;

import com.velocitypowered.api.command.CommandSource;
import dev.rollczi.litecommands.LiteSender;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class VelocitySender implements LiteSender {

    private final CommandSource source;

    public VelocitySender(CommandSource source) {
        this.source = source;
    }

    public boolean hasPermission(String permission) {
        return source.hasPermission(permission);
    }

    public void sendMessage(String message) {
        source.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(message));
    }

    public CommandSource getSender() {
        return source;
    }

}
