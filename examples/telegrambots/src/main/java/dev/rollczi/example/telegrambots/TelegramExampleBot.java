package dev.rollczi.example.telegrambots;

import dev.rollczi.litecommands.telegrambots.LiteTelegramRegister;
import java.util.List;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class TelegramExampleBot implements LongPollingUpdateConsumer {

    private final LiteTelegramRegister handler;

    public TelegramExampleBot(TelegramClient client) {

    }

    @Override
    public void consume(List<Update> updates) {

    }
}
