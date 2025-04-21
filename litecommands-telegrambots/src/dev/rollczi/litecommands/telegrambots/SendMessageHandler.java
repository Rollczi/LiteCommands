package dev.rollczi.litecommands.telegrambots;

import dev.rollczi.litecommands.handler.result.ResultHandler;
import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invocation.Invocation;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

class SendMessageHandler implements ResultHandler<User, BotApiMethod<?>> {

    private final TelegramClient client;

    public SendMessageHandler(TelegramClient client) {
        this.client = client;
    }

    @Override
    public void handle(Invocation<User> invocation, BotApiMethod<?> result, ResultHandlerChain<User> chain) {
        try {
            client.execute(result);
        } catch (TelegramApiException exception) {
            chain.resolve(invocation, exception);
        }
    }

}
