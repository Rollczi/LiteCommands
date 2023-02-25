package dev.rollczi.litecommands.modern.platform;

import dev.rollczi.litecommands.modern.command.CommandRoute;

public interface Platform<SENDER> {

    void registerExecuteListener(CommandRoute<SENDER> commandRoute, PlatformInvocationListener<SENDER> executeListener);

    void registerSuggestionListener(CommandRoute<SENDER> commandRoute, PlatformSuggestListener<SENDER> suggestListener);

}
