package dev.rollczi.litecommands.argument.parser;

import dev.rollczi.litecommands.argument.ArgumentKey;
import dev.rollczi.litecommands.shared.BiHashMap;
import dev.rollczi.litecommands.shared.BiMap;
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

        private final BucketByArgumentUniversal<PARSED> universalTypedBucket = new BucketByArgumentUniversal<>(true);

        private BucketByArgument() {
            super(false);
        }

        @Override
        void registerParser(Class<PARSED> parsedType, ArgumentKey key, Parser<SENDER, ?, PARSED> parser) {
            if (key.isUniversal()) {
                this.universalTypedBucket.registerParser(parsedType, key, parser);
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

            return universalTypedBucket.getParserSet(parsedType, key);
        }

    }

    private class BucketByArgumentUniversal<PARSED> {

        private final BiMap<String, String, ParserSetImpl<SENDER, PARSED>> buckets = new BiHashMap<>();
        private final boolean ignoreNamespace;

        private BucketByArgumentUniversal(boolean ignoreNamespace) {
            this.ignoreNamespace = ignoreNamespace;
        }

        void registerParser(Class<PARSED> parsedType, ArgumentKey key, Parser<SENDER, ?, PARSED> parser) {
            ParserSetImpl<SENDER, PARSED> bucket = buckets.computeIfAbsent(key.getKey(), key.getNamespace(), (k1, k2) -> new ParserSetImpl<>(parsedType));

            bucket.registerParser(parser);
        }

        ParserSetImpl<SENDER, PARSED> getParserSet(Class<PARSED> parsedType, ArgumentKey key) {
            String namespace = ignoreNamespace ? ArgumentKey.UNIVERSAL_NAMESPACE : key.getNamespace();
            ParserSetImpl<SENDER, PARSED> bucket = buckets.get(key.getKey(), namespace);

            if (bucket != null) {
                return bucket;
            }

            return buckets.get(StringUtil.EMPTY, namespace);
        }
    }

}
