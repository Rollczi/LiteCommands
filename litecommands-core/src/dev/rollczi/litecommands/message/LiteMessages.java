package dev.rollczi.litecommands.message;

import dev.rollczi.litecommands.permission.MissingPermissions;

public class LiteMessages {

    /**
     * Default message key for missing permissions.
     * It's used in {@link dev.rollczi.litecommands.permission.MissingPermissionResultHandler}
     */
    public static final MessageKey<MissingPermissions> MISSING_PERMISSIONS = MessageKey.of(
        "missing-permission",
        missingPermissions -> String.format("You don't have permission to execute this command! (%s)", missingPermissions.asJoinedText())
    );

    /**
     * Default message key for invalid number.
     * It's used in {@link dev.rollczi.litecommands.argument.resolver.std.NumberArgumentResolver}
     */
    public static final MessageKey<String> INVALID_NUMBER = MessageKey.of("invalid-number", input -> String.format("'%s' is not a number!", input));

    protected LiteMessages() {
    }

}
