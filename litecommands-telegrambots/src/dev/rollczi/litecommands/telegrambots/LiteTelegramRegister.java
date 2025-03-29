package dev.rollczi.litecommands.telegrambots;

import java.util.List;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface LiteTelegramRegister {

    void register(Handler handler) throws Exception;

    interface Handler {
        void handle(List<Update> updates);
    }

    // Formy organizacyjno prawne prowadzenie działalności gospodarczej
}
