package dev.rollczi.example.telegrambots;

import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.telegrambots.LiteTelegramBotsFactory;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class TelegramExampleMain {

    public static void main(String[] args) throws TelegramApiException {
        String token = "token";
        TelegramBotsLongPollingApplication app = new TelegramBotsLongPollingApplication();
        TelegramClient client = new OkHttpTelegramClient(token);

        LiteCommands<User> liteCommands = LiteTelegramBotsFactory.builder(client, handler -> app.registerBot(token, updates -> handler.handle(updates)))
            .build();

    }

}
