package dev.rollczi.litecommands.telegrambots;

import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.LiteCommandsFactory;
import org.telegram.telegrambots.meta.api.objects.User;

public final class LiteTelegramBotsFactory {

    private LiteTelegramBotsFactory() {
    }

    @SuppressWarnings("unchecked")
    public static <B extends LiteCommandsBuilder<User, LiteTelegramBotsSettings, B>> B builder(LiteTelegramBotsSettings liteTelegramBotsSettings) {
        return (B) LiteCommandsFactory.builder(User.class, new TelegramBotsPlatform(liteTelegramBotsSettings));
    }

}
