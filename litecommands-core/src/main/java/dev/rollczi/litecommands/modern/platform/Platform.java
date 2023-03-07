package dev.rollczi.litecommands.modern.platform;

import dev.rollczi.litecommands.modern.command.CommandRoute;

public interface Platform<SENDER> {

    void listenExecute(CommandRoute<SENDER> commandRoute, PlatformInvocationListener<SENDER> executeListener);

    void listenSuggestion(CommandRoute<SENDER> commandRoute, PlatformSuggestListener<SENDER> suggestListener);

    void unregisterAll();

}
