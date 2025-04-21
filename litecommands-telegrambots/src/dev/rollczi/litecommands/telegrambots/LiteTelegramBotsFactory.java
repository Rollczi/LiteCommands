package dev.rollczi.litecommands.telegrambots;

import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.LiteCommandsFactory;
import dev.rollczi.litecommands.context.ContextResult;
import dev.rollczi.litecommands.invocation.Invocation;
import java.util.function.Function;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.message.Message;
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
            .context(Update.class, invocation -> extract(invocation, update -> update))
            .context(Message.class, invocation -> extract(invocation, update -> update.getMessage()))
            .bind(TelegramClient.class, () -> client);
    }

    private static <T> ContextResult<T> extract(Invocation<User> invocation, Function<Update, T> extractor) {
        return invocation.context().get(Update.class)
            .map(t -> ContextResult.ok(() -> extractor.apply(t)))
            .orElseGet(() -> ContextResult.error(Update.class.getSimpleName() + " is not present"));
    }

}
