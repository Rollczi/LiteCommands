package dev.rollczi.litecommands.sponge;

import dev.rollczi.litecommands.message.LiteMessages;
import dev.rollczi.litecommands.message.MessageKey;

public class LiteSpongeMessages extends LiteMessages {

    public static final MessageKey<String> PLAYER_NOT_FOUND = MessageKey.of(
        "player-not-found",
        input -> "&cPlayer " + input + " not found! (PLAYER_NOT_FOUND)"
    );

    public static final MessageKey<Void> PLAYER_ONLY = MessageKey.of(
        "only-player",
        unused -> "&cOnly player can execute this command! (PLAYER_ONLY)"
    );

    public static final MessageKey<Void> SERVER_UNAVAILABLE = MessageKey.of(
        "server-unavailable",
        unused -> "&cServer isn't available on this environment! (SERVER_UNAVAILABLE)"
    );
}
