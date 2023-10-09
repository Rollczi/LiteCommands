package dev.rollczi.litecommands.message;

import dev.rollczi.litecommands.invalidusage.InvalidUsage;
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
     * It's used in {@link dev.rollczi.litecommands.argument.resolver.standard.NumberArgumentResolver}
     */
    public static final MessageKey<String> INVALID_NUMBER = MessageKey.of("invalid-number", input -> String.format("'%s' is not a number!", input));

    /**
     * Default message key for invalid usage.
     * It's used in {@link dev.rollczi.litecommands.invalidusage.InvalidUsageHandlerImpl}
     */
    public static final MessageKey<InvalidUsage<?>> INVALID_USAGE = MessageKey.of("invalid-usage", invalidUsage -> "Invalid usage of command!");

    protected LiteMessages() {
    }

}
