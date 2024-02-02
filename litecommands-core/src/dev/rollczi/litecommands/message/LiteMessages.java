package dev.rollczi.litecommands.message;

import dev.rollczi.litecommands.argument.resolver.standard.InstantArgumentResolver;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.permission.MissingPermissions;

public class LiteMessages {

    /**
     * Missing permissions message.
     * e.g. when user doesn't have permission to execute command.
     * @see dev.rollczi.litecommands.permission.MissingPermissionResultHandler
     */
    public static final MessageKey<MissingPermissions> MISSING_PERMISSIONS = MessageKey.of(
        "missing-permission",
        missingPermissions -> String.format("You don't have permission to execute this command! (%s) (MISSING_PERMISSIONS)", missingPermissions.asJoinedText())
    );

    /**
     * Invalid number.
     * @see dev.rollczi.litecommands.argument.resolver.standard.NumberArgumentResolver
     */
    public static final MessageKey<String> INVALID_NUMBER = MessageKey.of(
        "invalid-number",
        input -> String.format("'%s' is not a number! (INVALID_NUMBER)", input)
    );

    /**
     * Invalid usage of command.
     * e.g. when user provides invalid argument, doesn't provide required argument, provide not existing subcommand.
     * @see dev.rollczi.litecommands.invalidusage.InvalidUsageHandlerImpl
     */
    public static final MessageKey<InvalidUsage<?>> INVALID_USAGE = MessageKey.of(
        "invalid-usage",
        invalidUsage -> "Invalid usage of command! (INVALID_USAGE)"
    );

    /**
     * Invalid Instant format.
     * @see InstantArgumentResolver
     */
    public static final MessageKey<String> INSTANT_INVALID_FORMAT = MessageKey.of(
        "instant-invalid-format",
        input -> "Invalid date format '" + input + "'! Use: <yyyy-MM-dd> <HH:mm:ss> (INSTANT_INVALID_FORMAT)"
    );

    /**
     * Default message key for invalid Instant format.
     * It's used in {@link InstantArgumentResolver}
     */
    public static final MessageKey<String> INSTANT_INVALID_FORMAT = MessageKey.of(
        "instant-invalid-format",
        input -> "&cInvalid date format '" + input + "'! Use: <yyyy-MM-dd> <HH:mm:ss> (INSTANT_INVALID_FORMAT)"
    );

    protected LiteMessages() {
    }

}
