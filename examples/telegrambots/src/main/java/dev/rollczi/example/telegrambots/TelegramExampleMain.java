package dev.rollczi.example.telegrambots;

import dev.rollczi.example.telegrambots.command.TellCommand;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.telegrambots.LiteTelegramBotsFactory;
import dev.rollczi.litecommands.telegrambots.LiteTelegramDriver;
import java.util.List;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class TelegramExampleMain {

    public static void main(String[] args) {
        String token = "token";
        TelegramClient client = new OkHttpTelegramClient(token);
        TelegramBotsLongPollingApplication app = new TelegramBotsLongPollingApplication();
        TelegramExampleBot bot = new TelegramExampleBot(client);

        LiteTelegramDriver driver = new LiteTelegramDriver()
            .register(handler -> app.registerBot(token, updates -> handler.process(updates)))
            .unregister(() -> app.unregisterBot(token))
            .whenUnhandled(updates -> bot.consume(updates)); // handle unhandled updates here that were not processed by any command

        LiteCommands<User> liteCommands = LiteTelegramBotsFactory.builder(client, driver)
            .commands(new TellCommand())
            .build();

    }

}
