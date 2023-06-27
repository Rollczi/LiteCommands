package dev.rollczi.litecommands.argument.suggester;

import dev.rollczi.litecommands.argument.ArgumentKey;

public interface SuggesterRegistry<SENDER> {

    <T> void registerSuggester(Class<T> type, ArgumentKey key, Suggester<SENDER, T> suggester);

    <PARSED> Suggester<SENDER, PARSED> getSuggester(Class<PARSED> parsedClass, ArgumentKey key);

}
