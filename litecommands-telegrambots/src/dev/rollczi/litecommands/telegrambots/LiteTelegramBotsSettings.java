package dev.rollczi.litecommands.telegrambots;

import dev.rollczi.litecommands.platform.PlatformSettings;

public class LiteTelegramBotsSettings implements PlatformSettings {

    private final String commandPrefix = "/";

    public LiteTelegramBotsSettings commandPrefix(String prefix) {
        return this;
    }

    String getCommandPrefix() {
        return commandPrefix;
    }

}
