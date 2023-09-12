package dev.rollczi.litecommands.bukkit;

import dev.rollczi.litecommands.message.LiteMessages;
import dev.rollczi.litecommands.message.MessageKey;
import org.bukkit.World;

public class LiteBukkitMessages extends LiteMessages {

    /**
     * Default message key for world not exist.
     */
    public static final MessageKey<String> WORLD_NOT_EXIST = MessageKey.of("world-not-exist", input -> "&cWorld " + input + " doesn't exist!");

    /**
     * Default message key for invalid location.
     */
    public static final MessageKey<String> LOCATION_INVALID_FORMAT = MessageKey.of("location-invalid-format", input -> "&cInvalid location format '" + input + "'! Use: <x> <y> <z>");

}
