package dev.rollczi.litecommands.minestom;

import dev.rollczi.litecommands.message.LiteMessages;
import dev.rollczi.litecommands.message.MessageKey;
import net.minestom.server.entity.Player;

public class LiteMinestomMessages extends LiteMessages {

    public static final MessageKey<Player> PLAYER_IS_NOT_IN_INSTANCE = MessageKey.of(
        "player-is-not-in-instance",
        player -> "&cPlayer " + player.getUsername() + " is not in instance! (PLAYER_IS_NOT_IN_INSTANCE)"
    );

    public static final MessageKey<String> INSTANCE_NOT_FOUND = MessageKey.of(
        "instance-not-found",
        input -> "&cInstance " + input + " not found! (INSTANCE_NOT_FOUND)"
    );

    public static final MessageKey<String> PLAYER_NOT_FOUND = MessageKey.of(
        "player-not-found",
        input -> "&cPlayer " + input + " not found! (PLAYER_NOT_FOUND)"
    );

    public static final MessageKey<Void> PLAYER_ONLY = MessageKey.of(
        "only-player",
        unused -> "&cOnly player can execute this command! (PLAYER_ONLY)"
    );

    public static final MessageKey<Void> CONSOLE_ONLY = MessageKey.of(
        "only-player",
        unused -> "&cOnly the console can execute this command! (CONSOLE_ONLY)"
    );

}
