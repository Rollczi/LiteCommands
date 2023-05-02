package dev.rollczi.litecommands.argument.parser;

import dev.rollczi.litecommands.argument.ArgumentKey;

import java.util.HashMap;
import java.util.Map;

class ArgumentParserRegistryImpl<SENDER> implements ArgumentParserRegistry<SENDER> {

    private final Map<Class<?>, BucketByArgument<?>> buckets = new HashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public <PARSED> void registerParser(Class<PARSED> parserType, ArgumentKey key, ArgumentParser<SENDER, ?, PARSED> parser) {
        BucketByArgument<PARSED> bucket = (BucketByArgument<PARSED>) buckets.computeIfAbsent(parserType, k -> new BucketByArgument<>(parserType));

        bucket.registerParser(parserType, key, parser);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <PARSED> ArgumentParserSet<SENDER, PARSED> getParserSet(Class<PARSED> parserType, ArgumentKey key) {
        BucketByArgument<PARSED> bucket = (BucketByArgument<PARSED>) buckets.computeIfAbsent(parserType, k -> new BucketByArgument<>(parserType));

        return bucket.getParserSet(parserType, key);
    }

    private class BucketByArgument<PARSED> {

        private final ArgumentParserSetImpl<SENDER, PARSED> universalBucket;
        private final Map<ArgumentKey, ArgumentParserSetImpl<SENDER, PARSED>> buckets = new HashMap<>();

        private BucketByArgument(Class<PARSED> parsedClass) {
            this.universalBucket = new ArgumentParserSetImpl<>(parsedClass);
        }

        void registerParser(Class<PARSED> parsedType, ArgumentKey key, ArgumentParser<SENDER, ?, PARSED> parser) {
            if (key.isUniversal()) {
                this.universalBucket.registerParser(parser);
                return;
            }

            ArgumentParserSetImpl<SENDER, PARSED> bucket = buckets.computeIfAbsent(key, k -> new ArgumentParserSetImpl<>(parsedType, this.universalBucket));

            bucket.registerParser(parser);
        }

        ArgumentParserSetImpl<SENDER, PARSED> getParserSet(Class<PARSED> parsedType, ArgumentKey key) {
            return buckets.computeIfAbsent(key, k -> new ArgumentParserSetImpl<>(parsedType, this.universalBucket));
        }
    }

}
