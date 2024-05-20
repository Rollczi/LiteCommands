package dev.rollczi.litecommands.message;

import dev.rollczi.litecommands.argument.resolver.standard.InstantArgumentResolver;
import dev.rollczi.litecommands.argument.resolver.standard.UUIDArgumentResolver;
import dev.rollczi.litecommands.cooldown.CooldownState;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.permission.MissingPermissions;
import dev.rollczi.litecommands.time.DurationParser;

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
     * Command cooldown message.
     * e. g. when user uses the command too frequently.
     * @see dev.rollczi.litecommands.cooldown.CooldownStateResultHandler
     */
    public static final MessageKey<CooldownState> COMMAND_COOLDOWN = MessageKey.of(
        "command-cooldown",
        state -> "You are on cooldown! Remaining time: " + DurationParser.DATE_TIME_UNITS.format(state.getRemainingDuration()) + " (COMMAND_COOLDOWN)"
    );

    /**
     * Invalid UUID format.
     * @see UUIDArgumentResolver
     */
    public static final MessageKey<String> UUID_INVALID_FORMAT = MessageKey.of(
        "uuid-invalid-format",
        input -> "Invalid UUID format '" + input + "'! Use: xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxx (dashes are optional) (UUID_INVALID_FORMAT)"
    );

    protected LiteMessages() {
    }

}
