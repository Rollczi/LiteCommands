package dev.rollczi.litecommands.jda;

import dev.rollczi.litecommands.jda.permission.DiscordMissingPermissions;
import dev.rollczi.litecommands.message.LiteMessages;
import dev.rollczi.litecommands.message.MessageKey;

public class LiteJDAMessages extends LiteMessages {

    /**
     * Default message key for missing permissions.
     * It's used in {@link dev.rollczi.litecommands.permission.MissingPermissionResultHandler}
     */
    public static final MessageKey<DiscordMissingPermissions> DISCORD_MISSING_PERMISSIONS = MessageKey.of(
        "missing-permission",
        missingPermissions -> String.format("You don't have permission to execute this command! (%s)", missingPermissions.asJoinedText())
    );

    protected LiteJDAMessages() {
    }

}
