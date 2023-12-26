package dev.rollczi.litecommands.jakarta;

import dev.rollczi.litecommands.message.LiteMessages;
import dev.rollczi.litecommands.message.MessageKey;

public class LiteJakartaMessages extends LiteMessages {

    public static final MessageKey<String> CONSTRAINT_VIOLATIONS = MessageKey.of(
        "constraint-violations",
        input -> String.format("&cConstraint violations: %n%s (CONSTRAINT_VIOLATIONS)", input)
    );

}
