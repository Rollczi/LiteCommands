package dev.rollczi.litecommands.telegrambots;

import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.platform.PlatformInvocationListener;
import dev.rollczi.litecommands.platform.PlatformSuggestionListener;
import org.telegram.telegrambots.meta.api.objects.User;

record TelegramBotsCommand(
    CommandRoute<User> commandRoute,
    PlatformInvocationListener<User> invocationHook,
    PlatformSuggestionListener<User> suggestionHook
) {
}
