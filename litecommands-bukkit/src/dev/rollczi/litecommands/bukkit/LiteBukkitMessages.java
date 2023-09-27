package dev.rollczi.litecommands.bukkit;

import dev.rollczi.litecommands.message.LiteMessages;
import dev.rollczi.litecommands.message.MessageKey;

public class LiteBukkitMessages extends LiteMessages {

    public static final MessageKey<String> WORLD_NOT_EXIST = MessageKey.of(
        "world-not-exist",
        input -> "&cWorld " + input + " doesn't exist! (WORLD_NOT_EXIST)"
    );

    public static final MessageKey<String> LOCATION_INVALID_FORMAT = MessageKey.of(
        "location-invalid-format",
        input -> "&cInvalid location format '" + input + "'! Use: <x> <y> <z> (LOCATION_INVALID_FORMAT)"
    );

    public static final MessageKey<String> PLAYER_NOT_FOUND = MessageKey.of(
        "player-not-found",
        input -> "&cPlayer " + input + " not found! (PLAYER_NOT_FOUND)"
    );

    public static final MessageKey<Void> PLAYER_ONLY = MessageKey.of(
        "only-player",
        "&cOnly player can execute this command! (PLAYER_ONLY)"
    );

}
