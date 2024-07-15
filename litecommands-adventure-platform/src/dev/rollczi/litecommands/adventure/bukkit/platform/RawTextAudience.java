package dev.rollczi.litecommands.adventure.bukkit.platform;

import dev.rollczi.litecommands.platform.PlatformReceiver;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.MessageType;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Experimental
class RawTextAudience implements Audience {

    private final PlatformReceiver platformReceiver;

    public RawTextAudience(PlatformReceiver platformReceiver) {
        this.platformReceiver = platformReceiver;
    }

    @Override
    public void sendMessage(@NotNull Identity source, @NotNull Component message, @NotNull MessageType type) {
        platformReceiver.sendMessage(message.toString());
    }

}
