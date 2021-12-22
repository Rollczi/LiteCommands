package dev.rollczi.litecommands.valid;


import dev.rollczi.litecommands.valid.messages.LiteMessage;
import panda.utilities.StringUtils;

public enum ValidationInfo {

    COMMAND_NO_EXIST(result -> "&cCommand or subcommand not found"),
    NO_PERMISSION(result -> "&cYou don't have permission to this command"),
    INVALID_USE(result -> "&cInvalid use of the command"),
    CUSTOM,
    INTERNAL_ERROR,
    NONE;

    private final LiteMessage defaultMessage;

    ValidationInfo(LiteMessage defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    ValidationInfo() {
        this.defaultMessage = result -> StringUtils.EMPTY;
    }

    public LiteMessage getDefaultMessage() {
        return defaultMessage;
    }

}
