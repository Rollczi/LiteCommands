package dev.rollczi.litecommands.argument.parser;

import dev.rollczi.litecommands.argument.ArgumentKey;
import dev.rollczi.litecommands.shared.BiHashMap;
import dev.rollczi.litecommands.shared.BiMap;
import org.jetbrains.annotations.Nullable;

class ParserNamespacedIndex<SENDER, PARSED> {

    private final BiMap<String, String, ParserSetImpl<SENDER, PARSED>> unNamespacedIndex = new BiHashMap<>();
    private final BiMap<String, String, ParserSetImpl<SENDER, PARSED>> namespacedIndex = new BiHashMap<>();

    void registerParser(ArgumentKey key, Parser<SENDER, PARSED> parser) {
        if (key.isDefaultNamespace()) {
            ParserSetImpl<SENDER, PARSED> bucket = unNamespacedIndex.computeIfAbsent(key.getKey(), key.getNamespace(), (k1, k2) -> new ParserSetImpl<>());

            bucket.registerParser(parser);
            return;
        }

        ParserSetImpl<SENDER, PARSED> bucket = namespacedIndex.computeIfAbsent(key.getKey(), key.getNamespace(), (k1, k2) -> new NamespacedParserSet<>(key.getNamespace()));

        bucket.registerParser(parser);
    }

    @Nullable
    ParserSet<SENDER, PARSED> getParserSet(ArgumentKey key) {
        ParserSet<SENDER, PARSED> parserSet = namespacedIndex.get(key.getKey(), key.getNamespace());

        if (parserSet != null) {
            return parserSet;
        }

        parserSet = namespacedIndex.get(ArgumentKey.DEFAULT_KEY, key.getNamespace());

        if (parserSet != null) {
            return parserSet;
        }

        parserSet = unNamespacedIndex.get(key.getKey(), ArgumentKey.DEFAULT_NAMESPACE);

        if (parserSet != null) {
            return parserSet;
        }

        parserSet = unNamespacedIndex.get(ArgumentKey.DEFAULT_KEY, ArgumentKey.DEFAULT_NAMESPACE);

        if (parserSet != null) {
            return parserSet;
        }

        return new EmptyParserSetImpl<>();
    }

}
