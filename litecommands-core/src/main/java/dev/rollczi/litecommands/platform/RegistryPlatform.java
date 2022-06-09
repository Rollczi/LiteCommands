package dev.rollczi.litecommands.platform;

import dev.rollczi.litecommands.command.section.CommandSection;

public interface RegistryPlatform<SENDER> {

    void registerListener(CommandSection<SENDER> command, ExecuteListener<SENDER> executeListener, SuggestionListener<SENDER> suggestionListener);

    void unregisterListener(CommandSection<SENDER> command);

    void unregisterAll();

}
