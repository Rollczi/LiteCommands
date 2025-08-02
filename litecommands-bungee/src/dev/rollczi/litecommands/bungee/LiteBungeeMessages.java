package dev.rollczi.litecommands.bungee;

import dev.rollczi.litecommands.message.LiteMessages;
import dev.rollczi.litecommands.message.MessageKey;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class LiteBungeeMessages extends LiteMessages {

    public static final MessageKey<String> PLAYER_NOT_FOUND = MessageKey.of(
        "player-not-found",
        input -> "&cPlayer " + input + " not found! (PLAYER_NOT_FOUND)"
    );

    public static final MessageKey<Void> PLAYER_ONLY = MessageKey.of(
        "only-player",
        unused -> "&cOnly player can execute this command! (PLAYER_ONLY)"
    );

    public static final MessageKey<ProxiedPlayer> NOT_CONNECTED_TO_ANY_SERVER = MessageKey.of(
        "not-connected-to-any-server",
        player -> "&cYou are not connected to any server! (NOT_CONNECTED_TO_ANY_SERVER)"
    );

    public static final MessageKey<String> SERVER_NOT_FOUND = MessageKey.of(
        "server-not-found",
        input -> "&cServer " + input + " not found! (SERVER_NOT_FOUND)"
    );

}
