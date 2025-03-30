package dev.rollczi.litecommands.telegrambots;

import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.LiteCommandsFactory;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public final class LiteTelegramBotsFactory {

    private LiteTelegramBotsFactory() {
    }

    public static <B extends LiteCommandsBuilder<User, LiteTelegramBotsSettings, B>> B builder(TelegramClient client, LiteTelegramDriver driver) {
        return builder(client, driver, new LiteTelegramBotsSettings());
    }

    @SuppressWarnings("unchecked")
    public static <B extends LiteCommandsBuilder<User, LiteTelegramBotsSettings, B>> B builder(
        TelegramClient client,
        LiteTelegramDriver driver,
        LiteTelegramBotsSettings settings
    ) {
        return (B) LiteCommandsFactory.builder(User.class, new TelegramBotsPlatform(driver, settings))
            .result(String.class, new StringHandler(client))
            .result(BotApiMethod.class, new SendMessageHandler(client))
            .bind(TelegramClient.class, () -> client);
    }

}
