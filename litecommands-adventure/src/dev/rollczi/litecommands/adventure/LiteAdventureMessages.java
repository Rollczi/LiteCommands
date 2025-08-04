package dev.rollczi.litecommands.adventure;

import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.message.LiteMessages;
import dev.rollczi.litecommands.message.MessageKey;

public class LiteAdventureMessages extends LiteMessages {
    public static final MessageKey<RawInput> NAMESPACED_KEY_INVALID = MessageKey.of(
        "namespaced-key-invalid",
        unused -> "&cNamespaced key is not valid! (NAMESPACED_KEY_INVALID)"
    );
}
