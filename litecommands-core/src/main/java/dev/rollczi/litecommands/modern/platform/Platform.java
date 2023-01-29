package dev.rollczi.litecommands.modern.platform;

import dev.rollczi.litecommands.modern.command.CommandRoute;

public interface Platform<SENDER> {

    void registerExecuteListener(CommandRoute commandRoute, PlatformExecuteListener<SENDER> executeListener);

    void registerSuggestionListener(CommandRoute commandRoute, PlatformSuggestListener<SENDER> suggestListener);

}
