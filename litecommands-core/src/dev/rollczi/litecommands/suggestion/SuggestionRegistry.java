package dev.rollczi.litecommands.suggestion;

import dev.rollczi.litecommands.argument.ArgumentKey;

public interface SuggestionRegistry<SENDER> {

    <T> void registerSuggester(Class<T> type, ArgumentKey key, ArgumentSuggester<SENDER, T> suggester);

}
