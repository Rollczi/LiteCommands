package dev.rollczi.litecommands.argument.suggester;

import dev.rollczi.litecommands.argument.ArgumentKey;
import dev.rollczi.litecommands.shared.BiHashMap;
import dev.rollczi.litecommands.shared.BiMap;
import dev.rollczi.litecommands.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

public class SuggesterRegistryImpl<SENDER> implements SuggesterRegistry<SENDER> {

    private final Suggester<SENDER, ?> noneSuggester = new SuggesterNoneImpl<>();

    private final Map<Class<?>, BucketByArgument<?>> buckets = new HashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public <T> void registerSuggester(Class<T> type, ArgumentKey key, Suggester<SENDER, T> suggester) {
        BucketByArgument<T> bucket = (BucketByArgument<T>) buckets.computeIfAbsent(type, k -> new BucketByArgument<>());

        bucket.registerSuggester(type, key, suggester);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <PARSED> Suggester<SENDER, PARSED> getSuggester(Class<PARSED> parsedClass, ArgumentKey key) {
        BucketByArgument<PARSED> bucket = (BucketByArgument<PARSED>) buckets.get(parsedClass);

        if (bucket == null) {
            return (Suggester<SENDER, PARSED>) noneSuggester;
        }

        Suggester<SENDER, PARSED> suggester = bucket.getSuggester(key);

        if (suggester == null) {
            return (Suggester<SENDER, PARSED>) noneSuggester;
        }

        return suggester;
    }

    private class BucketByArgument<PARSED> extends BucketByArgumentUniversal<PARSED> {

        private final BucketByArgumentUniversal<PARSED> universalTypedBucket = new BucketByArgumentUniversal<>(true);

        private BucketByArgument() {
            super(false);
        }

        @Override
        void registerSuggester(Class<PARSED> parsedType, ArgumentKey key, Suggester<SENDER, PARSED> parser) {
            if (key.isUniversal()) {
                this.universalTypedBucket.registerSuggester(parsedType, key, parser);
                return;
            }

            super.registerSuggester(parsedType, key, parser);
        }

        @Override
        Suggester<SENDER, PARSED> getSuggester(ArgumentKey key) {
            Suggester<SENDER, PARSED> bucket = super.getSuggester(key);

            if (bucket != null) {
                return bucket;
            }

            return universalTypedBucket.getSuggester(key);
        }

    }

    private class BucketByArgumentUniversal<PARSED> {

        private final BiMap<String, String, Suggester<SENDER, PARSED>> buckets = new BiHashMap<>();
        private final boolean ignoreNamespace;

        private BucketByArgumentUniversal(boolean ignoreNamespace) {
            this.ignoreNamespace = ignoreNamespace;
        }

        void registerSuggester(Class<PARSED> parsedType, ArgumentKey key, Suggester<SENDER, PARSED> parser) {
            buckets.put(key.getKey(), key.getNamespace(), parser);
        }

        Suggester<SENDER, PARSED> getSuggester(ArgumentKey key) {
            String namespace = ignoreNamespace ? ArgumentKey.UNIVERSAL_NAMESPACE : key.getNamespace();
            Suggester<SENDER, PARSED> bucket = buckets.get(key.getKey(), namespace);

            if (bucket != null) {
                return bucket;
            }

            return buckets.get(StringUtil.EMPTY, namespace);
        }
    }

}
