package dev.rollczi.litecommands.argument.suggester;

import dev.rollczi.litecommands.argument.ArgumentKey;
import dev.rollczi.litecommands.reflect.type.TypeRange;
import java.util.List;

public interface SuggesterRegistry<SENDER> {

    default <T> void registerSuggester(Class<T> type, ArgumentKey key, Suggester<SENDER, T> suggester) {
        registerSuggester(TypeRange.same(type), key, suggester);
    }

    <T> void registerSuggester(TypeRange<T> type, ArgumentKey key, Suggester<SENDER, T> suggester);

    default <PARSED> Suggester<SENDER, PARSED> getSuggester(Class<PARSED> parsedClass, ArgumentKey key) {
        return getSuggesters(parsedClass, key).stream()
            .findFirst()
            .orElse(new SuggesterNoneImpl<>());
    }

    <PARSED> List<Suggester<SENDER, PARSED>> getSuggesters(Class<PARSED> parsedClass, ArgumentKey key);

}
