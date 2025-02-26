package dev.rollczi.litecommands.telegrambots;

import dev.rollczi.litecommands.argument.parser.input.ParseableInput;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.platform.AbstractSimplePlatform;
import dev.rollczi.litecommands.platform.PlatformInvocationListener;
import dev.rollczi.litecommands.platform.PlatformSuggestionListener;
import dev.rollczi.litecommands.shared.Preconditions;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class TelegramBotsPlatform extends AbstractSimplePlatform<User, LiteTelegramBotsSettings> {

    private final Map<String, TelegramBotsCommand> commands = new ConcurrentHashMap<>();

    public TelegramBotsPlatform(LiteTelegramBotsSettings liteTelegramBotsSettings) {
        super(liteTelegramBotsSettings, sender -> new TelegramBotsSender(sender, liteTelegramBotsSettings));
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

    /**
     * @return true if input was handled (it was a command), false otherwise (it was a message or other content)
     */
    public boolean handleUpdate(Update update) {
        Preconditions.notNull(update, "update");

        if (!update.hasMessage()) return false;
        Message message = update.getMessage();

        if (!message.hasText()) return false;
        String text = message.getText();
        if (text.isEmpty()) return false;

        char firstChar = text.charAt(0);
        if (settings.getCommandPrefixes().stream().noneMatch(prefix -> prefix == firstChar)) return false;

        String label;
        ParseableInput<?> input;

        int firstIndexOfSpace = text.indexOf(' ');
        if (firstIndexOfSpace == -1) {
            label = text.substring(1);
            input = ParseableInput.raw(Collections.emptyList());
        } else {
            label = text.substring(1, firstIndexOfSpace + 1);
            input = ParseableInput.raw(text.substring(firstIndexOfSpace + 1).split(" "));
        }

        TelegramBotsCommand command = commands.get(label);
        if (command == null) return false; // todo: do something instead?

        User sender = message.getFrom();
        Invocation<User> invocation = new Invocation<>(sender, new TelegramBotsSender(sender, settings), command.commandRoute().getName(), label, input);
        command.invocationHook().execute(invocation, input);
        return true;
    }
}
