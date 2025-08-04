package dev.rollczi.litecommands.adventure;

import dev.rollczi.litecommands.message.LiteMessages;
import dev.rollczi.litecommands.message.MessageKey;

public class LiteAdventureMessages extends LiteMessages {

    public static final MessageKey<String> ADVENTURE_KEY_INVALID = MessageKey.of(
        "adventure-key-invalid",
        invalidKey -> "<red>Key '" + invalidKey + "' is not valid! (ADVENTURE_KEY_INVALID)"
    );

}
