package dev.rollczi.litecommands.hytale;

import dev.rollczi.litecommands.message.LiteMessages;
import dev.rollczi.litecommands.message.MessageKey;

public class LiteHytaleMessages extends LiteMessages {

    //FIXME: API-DROP (colors?)

    public static final MessageKey<String> PLAYER_NOT_FOUND = MessageKey.of(
        "player-not-found",
        input -> "Player " + input + " not found! (PLAYER_NOT_FOUND)"
    );

    public static final MessageKey<Void> PLAYER_ONLY = MessageKey.of(
        "only-player",
        unused -> "Only player can execute this command! (PLAYER_ONLY)"
    );

    public static final MessageKey<Void> CONSOLE_ONLY = MessageKey.of(
        "only-console",
        unused -> "Only console can execute this command! (CONSOLE_ONLY)"
    );
}
