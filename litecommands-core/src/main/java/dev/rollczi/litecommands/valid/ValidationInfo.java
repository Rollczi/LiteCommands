package dev.rollczi.litecommands.valid;

import panda.utilities.StringUtils;

public enum ValidationInfo {

    COMMAND_NO_FOUND("&cCommand or subcommand not found"),
    NO_PERMISSION("&cNie posiadasz permisji do tej komendy!"),
    INCORRECT_USE("&cNie poprawne uzycie komendy!"),
    CUSTOM(StringUtils.EMPTY);

    private final String defaultMessage;

    ValidationInfo(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

}
