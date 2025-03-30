package dev.rollczi.litecommands.telegrambots;

import dev.rollczi.litecommands.argument.parser.input.ParseableInput;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.input.raw.RawCommand;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.invocation.InvocationContext;
import dev.rollczi.litecommands.platform.AbstractSimplePlatform;
import dev.rollczi.litecommands.platform.PlatformInvocationListener;
import dev.rollczi.litecommands.platform.PlatformSuggestionListener;
import java.util.ArrayList;
import java.util.List;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class TelegramBotsPlatform extends AbstractSimplePlatform<User, LiteTelegramBotsSettings> {

    private final LiteTelegramDriver driver;
    private final Map<String, TelegramBotsCommand> commands = new ConcurrentHashMap<>();

    public TelegramBotsPlatform(LiteTelegramDriver driver, LiteTelegramBotsSettings settings) {
        super(settings, sender -> new TelegramBotsSender(sender));
        this.driver = driver;
    }

    @Override
    public void start() {
        try {
            driver.registerHandler(updates -> this.handleUpdates(updates));
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void stop() {
        try {
            driver.unregisterHandler();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    protected void hook(CommandRoute<User> commandRoute, PlatformInvocationListener<User> invocationHook, PlatformSuggestionListener<User> suggestionHook) {
        TelegramBotsCommand command = new TelegramBotsCommand(commandRoute, invocationHook, suggestionHook);
        for (String label : commandRoute.names()) {
            commands.put(label, command);
        }
    }

    @Override
    protected void unhook(CommandRoute<User> commandRoute) {
        for (String label : commandRoute.names()) {
            commands.remove(label);
        }
    }

    private List<Update> handleUpdates(List<Update> updates) {
        List<Update> unhandled = new ArrayList<>();
        for (Update update : updates) {
            if (this.handleUpdate(update)) {
                continue;
            }

            unhandled.add(update);
        }

        return unhandled;
    }

    private boolean handleUpdate(Update update) {
        if (!update.hasMessage()) {
            return false;
        }

        Message message = update.getMessage();
        String text = message.getText();
        if (text == null || !text.startsWith(settings.getCommandPrefix())) {
            return false;
        }

        RawCommand command = RawCommand.from(text);
        TelegramBotsCommand telegramCommand = commands.get(command.getLabel());
        if (telegramCommand == null) {
            return false;
        }

        User sender = message.getFrom();
        ParseableInput<?> input = command.toParseableInput();
        String name = telegramCommand.commandRoute().getName();
        Invocation<User> invocation = new Invocation<>(new TelegramBotsSender(sender), name, command.getLabel(), input, createContext(update));
        telegramCommand.invocationHook().execute(invocation, input);
        return true;
    }

    private static InvocationContext createContext(Update update) {
        return InvocationContext.builder()
            .put(Update.class, update)
            .build();
    }

}
