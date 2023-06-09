package dev.rollczi.litecommands.suggestion;

import dev.rollczi.litecommands.argument.ArgumentKey;

public class SuggestionRegistryImpl<SENDER> implements SuggestionRegistry<SENDER> {


    @Override
    public <T> void registerSuggester(Class<T> type, ArgumentKey key, ArgumentSuggester<SENDER, T> suggester) {

    }

}
