package dev.rollczi.litecommands.argument.suggester;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.ArgumentKey;
import dev.rollczi.litecommands.argument.suggester.input.SuggestionInputResult;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.reflect.type.TypeIndex;
import dev.rollczi.litecommands.reflect.type.TypeRange;
import dev.rollczi.litecommands.shared.BiHashMap;
import dev.rollczi.litecommands.shared.BiMap;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import dev.rollczi.litecommands.util.StringUtil;
import dev.rollczi.litecommands.wrapper.WrapFormat;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.Nullable;

public class SuggesterRegistryImpl<SENDER> implements SuggesterRegistry<SENDER>, SuggesterChainAccessor<SENDER> {

    private final Suggester<SENDER, ?> noneSuggester = new SuggesterNoneImpl<>();

    private final TypeIndex<BucketByArgument<?>> buckets = new TypeIndex<>();

    @Override
    @SuppressWarnings("unchecked")
    public <T> void registerSuggester(TypeRange<T> type, ArgumentKey key, Suggester<SENDER, T> suggester) {
        List<BucketByArgument<?>> bucketByArguments = buckets.computeIfAbsent(type, () -> new BucketByArgument<>());

        for (BucketByArgument<?> bucketByArgument : bucketByArguments) {
            BucketByArgument<T> bucket = (BucketByArgument<T>) bucketByArgument;
            bucket.registerSuggester(type, key, suggester);
        }

    }

    @Override
    public <T> void registerSuggester(TypeRange<T> type, ArgumentKey key, SuggesterChained<SENDER, T> suggester) {
        registerSuggester(type, key, (invocation, argument, context) -> suggester.suggest(invocation, argument, context, SuggesterRegistryImpl.this));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <PARSED> List<Suggester<SENDER, PARSED>> getSuggesters(Class<PARSED> parsedClass, ArgumentKey key) {
        Iterable<BucketByArgument<?>> typedBuckets = buckets.get(parsedClass);
        List<Suggester<SENDER, PARSED>> suggesters = new ArrayList<>();

        for (BucketByArgument<?> typedBucket : typedBuckets) {
            BucketByArgument<PARSED> bucket = (BucketByArgument<PARSED>) typedBucket;
            Suggester<SENDER, PARSED> suggester = bucket.getSuggester(key);

            if (suggester != null) {
                suggesters.add(suggester);
            }
        }

        return suggesters;
    }

    @Override
    public <T> SuggestionResult suggest(Invocation<SENDER> invocation, Argument<T> argument, SuggestionContext context) {
        Class<T> parsedType = argument.getWrapperFormat().getParsedType();
        Suggester<SENDER, T> suggester = getSuggester(parsedType, argument.getKey());

        return suggester.suggest(invocation, argument, context);
    }

    private class BucketByArgument<PARSED> extends BucketByArgumentUniversal<PARSED> {

        private final BucketByArgumentUniversal<PARSED> universalTypedBucket = new BucketByArgumentUniversal<>(true);

        private BucketByArgument() {
            super(false);
        }

        @Override
        void registerSuggester(TypeRange<PARSED> parsedType, ArgumentKey key, Suggester<SENDER, PARSED> parser) {
            if (key.isUniversal()) {
                this.universalTypedBucket.registerSuggester(parsedType, key, parser);
                return;
            }

            super.registerSuggester(parsedType, key, parser);
        }

        @Override
        @Nullable
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

        void registerSuggester(TypeRange<PARSED> parsedType, ArgumentKey key, Suggester<SENDER, PARSED> parser) {
            buckets.put(key.getKey(), key.getNamespace(), parser);
        }

        @Nullable
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
