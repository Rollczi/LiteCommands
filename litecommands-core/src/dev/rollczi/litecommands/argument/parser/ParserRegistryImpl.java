package dev.rollczi.litecommands.argument.parser;

import dev.rollczi.litecommands.argument.ArgumentKey;
import dev.rollczi.litecommands.shared.BiHashMap;
import dev.rollczi.litecommands.shared.BiMap;
import dev.rollczi.litecommands.util.MapUtil;
import dev.rollczi.litecommands.util.StringUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
        Optional<BucketByArgument<?>> argumentOptional = MapUtil.findBySuperTypeOf(parserType, buckets);

        if (argumentOptional.isPresent()) {
            BucketByArgument<PARSED> bucket = (BucketByArgument<PARSED>) argumentOptional.get();

            return bucket.getParserSet(key);
        }

        return new EmptyParserSetImpl<>();
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
        ParserSet<SENDER, PARSED> getParserSet(ArgumentKey key) {
            ParserSet<SENDER, PARSED> bucket = super.getParserSet(key);

            if (bucket != null) {
                return bucket;
            }

            return universalTypedBucket.getParserSet(key);
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

        ParserSet<SENDER, PARSED> getParserSet(ArgumentKey key) {
            String namespace = ignoreNamespace ? ArgumentKey.UNIVERSAL_NAMESPACE : key.getNamespace();
            ParserSetImpl<SENDER, PARSED> bucket = buckets.get(key.getKey(), namespace);

            if (bucket != null) {
                return bucket;
            }

            return buckets.get(StringUtil.EMPTY, namespace);
        }
    }

}
