package dev.rollczi.litecommands.argument.parser;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.ArgumentKey;
import dev.rollczi.litecommands.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

public class ParserRegistryImpl<SENDER> implements ParserRegistry<SENDER> {

    private final Map<Class<?>, BucketByArgument<?>> buckets = new HashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public <PARSED> void registerParser(Class<PARSED> parserType, ArgumentKey key, Parser<SENDER, ?, PARSED> parser) {
        BucketByArgument<PARSED> bucket = (BucketByArgument<PARSED>) buckets.computeIfAbsent(parserType, k -> new BucketByArgument<>());

        bucket.registerParser(parserType, key, parser);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <PARSED> ParserSet<SENDER, PARSED> getParserSet(Class<PARSED> parserType, ArgumentKey key) {
        BucketByArgument<PARSED> bucket = (BucketByArgument<PARSED>) buckets.computeIfAbsent(parserType, k -> new BucketByArgument<>());
        ParserSetImpl<SENDER, PARSED> parserSet = bucket.getParserSet(parserType, key);

        if (parserSet == null) {
            return new ParserSetImpl<>(parserType);
        }

        return parserSet;
    }

    private class BucketByArgument<PARSED> extends BucketByArgumentUniversal<PARSED> {

        private final BucketByArgumentUniversal<PARSED> universalTypedBucket = new BucketByArgumentUniversal<>();

        @Override
        void registerParser(Class<PARSED> parsedType, ArgumentKey key, Parser<SENDER, ?, PARSED> parser) {
            if (key.isUniversal()) {
                this.universalTypedBucket.registerParser(parsedType, key.withUniversalNamespace(), parser);
                return;
            }

            super.registerParser(parsedType, key, parser);
        }

        @Override
        ParserSetImpl<SENDER, PARSED> getParserSet(Class<PARSED> parsedType, ArgumentKey key) {
            ParserSetImpl<SENDER, PARSED> bucket = super.getParserSet(parsedType, key);

            if (bucket != null) {
                return bucket;
            }

            return universalTypedBucket.getParserSet(parsedType, key.withUniversalNamespace());
        }


    }

    private class BucketByArgumentUniversal<PARSED> {

        private final Map<ArgumentKey, ParserSetImpl<SENDER, PARSED>> buckets = new HashMap<>();

        void registerParser(Class<PARSED> parsedType, ArgumentKey key, Parser<SENDER, ?, PARSED> parser) {
            ParserSetImpl<SENDER, PARSED> bucket = buckets.computeIfAbsent(key, k -> new ParserSetImpl<>(parsedType));

            bucket.registerParser(parser);
        }

        ParserSetImpl<SENDER, PARSED> getParserSet(Class<PARSED> parsedType, ArgumentKey key) {
            ParserSetImpl<SENDER, PARSED> bucket = buckets.get(key);

            if (bucket != null) {
                return bucket;
            }

            return buckets.get(key.withKey(StringUtil.EMPTY));
        }
    }

}
