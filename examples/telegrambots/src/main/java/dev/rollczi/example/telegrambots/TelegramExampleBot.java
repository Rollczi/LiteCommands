package dev.rollczi.example.telegrambots;

import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class TelegramExampleBot implements LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient client;

    public TelegramExampleBot(TelegramClient client) {
        this.client = client;
    }

    @Override
    public void consume(Update update) {
        // other updates that were not processed by any command
    }

}
