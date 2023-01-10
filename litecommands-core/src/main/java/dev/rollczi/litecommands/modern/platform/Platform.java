package dev.rollczi.litecommands.modern.platform;

public interface Platform<SENDER> {

    void registerCommandExecuteListener(PlatformCommandExecuteListener<SENDER> executeListener);

    void registerCommandSuggestionListener(PlatformCommandSuggestListener<SENDER> suggestListener);

}
