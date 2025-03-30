package dev.rollczi.litecommands.telegrambots;

import dev.rollczi.litecommands.handler.result.ResultHandler;
import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invocation.Invocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

class StringHandler implements ResultHandler<User, String> {

    private final TelegramClient client;

    public StringHandler(TelegramClient client) {
        this.client = client;
    }

    @Override
    public void handle(Invocation<User> invocation, String result, ResultHandlerChain<User> chain) {
        Update update = invocation.context().get(Update.class)
            .orElseThrow(() -> new IllegalStateException("Context does not contain Update"));

        String chatId = update.getMessage().getChatId().toString();
        SendMessage message = SendMessage
            .builder()
            .chatId(chatId)
            .text(result)
            .build();

        try {
            client.execute(message);
        } catch (TelegramApiException exception) {
            chain.resolve(invocation, exception);
        }
    }

}
