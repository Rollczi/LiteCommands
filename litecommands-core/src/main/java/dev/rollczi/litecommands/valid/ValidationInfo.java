package dev.rollczi.litecommands.valid;

import panda.utilities.StringUtils;

public enum ValidationInfo {

    COMMAND_NO_FOUND("&cCommand or subcommand not found"),
    NO_PERMISSION("&cYou don't have permission to this command"),
    INVALID_USE("&cInvalid use of the command"),
    CUSTOM(StringUtils.EMPTY);

    private final String defaultMessage;

    ValidationInfo(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

}
