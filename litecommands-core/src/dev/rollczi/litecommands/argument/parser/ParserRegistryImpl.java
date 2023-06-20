package dev.rollczi.litecommands.argument.parser;

import dev.rollczi.litecommands.argument.ArgumentKey;

import java.util.HashMap;
import java.util.Map;

public class ParserRegistryImpl<SENDER> implements ParserRegistry<SENDER> {

    private final Map<Class<?>, BucketByArgument<?>> buckets = new HashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public <PARSED> void registerParser(Class<PARSED> parserType, ArgumentKey key, Parser<SENDER, ?, PARSED> parser) {
        BucketByArgument<PARSED> bucket = (BucketByArgument<PARSED>) buckets.computeIfAbsent(parserType, k -> new BucketByArgument<>(parserType));

        bucket.registerParser(parserType, key, parser);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <PARSED> ParserSet<SENDER, PARSED> getParserSet(Class<PARSED> parserType, ArgumentKey key) {
        BucketByArgument<PARSED> bucket = (BucketByArgument<PARSED>) buckets.computeIfAbsent(parserType, k -> new BucketByArgument<>(parserType));

        return bucket.getParserSet(parserType, key);
    }

    private class BucketByArgument<PARSED> {

        private final ParserSetImpl<SENDER, PARSED> universalBucket;
        private final Map<ArgumentKey, ParserSetImpl<SENDER, PARSED>> buckets = new HashMap<>();

        private BucketByArgument(Class<PARSED> parsedClass) {
            this.universalBucket = new ParserSetImpl<>(parsedClass);
        }

        void registerParser(Class<PARSED> parsedType, ArgumentKey key, Parser<SENDER, ?, PARSED> parser) {
            if (key.isUniversal()) {
                this.universalBucket.registerParser(parser);
                return;
            }

            ParserSetImpl<SENDER, PARSED> bucket = buckets.computeIfAbsent(key, k -> new ParserSetImpl<>(parsedType, this.universalBucket));

            bucket.registerParser(parser);
        }

        ParserSetImpl<SENDER, PARSED> getParserSet(Class<PARSED> parsedType, ArgumentKey key) {
            return buckets.computeIfAbsent(key, k -> new ParserSetImpl<>(parsedType, this.universalBucket));
        }
    }

}
