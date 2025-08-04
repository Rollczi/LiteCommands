package dev.rollczi.litecommands.bukkit;

import dev.rollczi.litecommands.message.LiteMessages;
import dev.rollczi.litecommands.message.MessageKey;

public class LiteBukkitMessages extends LiteMessages {

    public static final MessageKey<Void> PLAYER_ONLY = MessageKey.of(
        "only-player",
        unused -> "&cOnly player can execute this command! (PLAYER_ONLY)"
    );

    public static final MessageKey<Void> CONSOLE_ONLY = MessageKey.of(
        "only-console",
        unused -> "&cOnly console can execute this command! (CONSOLE_ONLY)"
    );

    public static final MessageKey<String> WORLD_NOT_EXIST = MessageKey.of(
        "world-not-exist",
        input -> "&cWorld " + input + " doesn't exist! (WORLD_NOT_EXIST)"
    );

    @Deprecated
    public static final MessageKey<Void> WORLD_PLAYER_ONLY = MessageKey.<Void>of(
        "world-player-only",
        unused -> "&cOnly player can execute this command! (WORLD_PLAYER_ONLY)"
    ).withFallbacks(PLAYER_ONLY);

    public static final MessageKey<Void> WORLD_NO_CONSOLE = MessageKey.<Void>of(
        "world-no-console",
        unused -> "&cConsole cannot execute this command! (WORLD_NO_CONSOLE)"
    ).withFallbacks(WORLD_PLAYER_ONLY, PLAYER_ONLY);

    public static final MessageKey<String> LOCATION_INVALID_FORMAT = MessageKey.of(
        "location-invalid-format",
        input -> "&cInvalid location format '" + input + "'! Use: <x> <y> <z> (LOCATION_INVALID_FORMAT)"
    );

    @Deprecated
    public static final MessageKey<Void> LOCATION_PLAYER_ONLY = MessageKey.<Void>of(
        "location-player-only",
        unused -> "&cOnly player can execute this command! (LOCATION_PLAYER_ONLY)"
    ).withFallbacks(PLAYER_ONLY);

    public static final MessageKey<Void> LOCATION_NO_CONSOLE = MessageKey.<Void>of(
        "location-no-console",
        unused -> "&cConsole cannot execute this command! (LOCATION_NO_CONSOLE)"
    ).withFallbacks(LOCATION_PLAYER_ONLY, PLAYER_ONLY);

    public static final MessageKey<String> PLAYER_NOT_FOUND = MessageKey.of(
        "player-not-found",
        input -> "&cPlayer " + input + " not found! (PLAYER_NOT_FOUND)"
    );

    public static final MessageKey<String> OFFLINE_PLAYER_NOT_FOUND = MessageKey.<String>of(
        "offline-player-not-found",
        input -> "&cPlayer " + input + " not found! (OFFLINE_PLAYER_NOT_FOUND)"
    ).withFallbacks(PLAYER_NOT_FOUND);

    public static final MessageKey<String> NAMESPACED_KEY_INVALID = MessageKey.of(
        "namespaced-key-invalid",
        unused -> "&cNamespaced key is not valid! (NAMESPACED_KEY_INVALID)"
    );
}
