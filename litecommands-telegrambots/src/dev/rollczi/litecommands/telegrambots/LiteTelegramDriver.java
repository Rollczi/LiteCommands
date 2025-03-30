package dev.rollczi.litecommands.telegrambots;

import java.util.List;
import java.util.function.Function;
import org.telegram.telegrambots.meta.api.objects.Update;

public class LiteTelegramDriver {

    private Register register = (handler) -> { throw new UnsupportedOperationException("Register not set"); };
    private Unregister unregister = () -> { throw new UnsupportedOperationException("Unregister not set"); };
    private Unhandled unhandled = (updates) -> { throw new UnsupportedOperationException("Unhandled not set"); };

    public LiteTelegramDriver register(Register register) {
        this.register = register;
        return this;
    }

    public LiteTelegramDriver unregister(Unregister unregister) {
        this.unregister = unregister;
        return this;
    }

    public LiteTelegramDriver whenUnhandled(Unhandled unhandled) {
        this.unhandled = unhandled;
        return this;
    }

    public void registerHandler(Function<List<Update>, List<Update>> updateHandler) throws Exception {
        this.register.register(updates -> {
            List<Update> unhandled = updateHandler.apply(updates);
            if (!unhandled.isEmpty()) {
                this.unhandled.unhandled(unhandled);
            }
        });
    }

    public void unregisterHandler() throws Exception {
        this.unregister.unregister();
    }

    public interface Register {
        void register(Handler handler) throws Exception;

        interface Handler {
            void process(List<Update> updates);
        }
    }

    public interface Unregister {
        void unregister() throws Exception;
    }

    public interface Unhandled {
        void unhandled(List<Update> updates);
    }

}
