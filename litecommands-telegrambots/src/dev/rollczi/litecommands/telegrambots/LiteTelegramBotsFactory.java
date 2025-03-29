package dev.rollczi.litecommands.telegrambots;

import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.LiteCommandsFactory;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public final class LiteTelegramBotsFactory {

    private LiteTelegramBotsFactory() {
    }

    public static <B extends LiteCommandsBuilder<User, LiteTelegramBotsSettings, B>> B builder(TelegramClient client, LiteTelegramRegister handler) {
        return builder(client, new LiteTelegramBotsSettings());
    }

    @SuppressWarnings("unchecked")
    public static <B extends LiteCommandsBuilder<User, LiteTelegramBotsSettings, B>> B builder(TelegramClient client, LiteTelegramBotsSettings liteTelegramBotsSettings) {
        return (B) LiteCommandsFactory.builder(User.class, new TelegramBotsPlatform(liteTelegramBotsSettings));
    }

}
