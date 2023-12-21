package dev.rollczi.litecommands.argument.parser;

import dev.rollczi.litecommands.argument.ArgumentKey;
import dev.rollczi.litecommands.reflect.type.TypeRange;
import dev.rollczi.litecommands.shared.BiHashMap;
import dev.rollczi.litecommands.shared.BiMap;
import dev.rollczi.litecommands.reflect.type.TypeIndex;
import dev.rollczi.litecommands.util.StringUtil;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ParserRegistryImpl<SENDER> implements ParserRegistry<SENDER> {

    private final TypeIndex<BucketByArgument<?>> buckets = new TypeIndex<>();

    @Override
    @SuppressWarnings("unchecked")
    @NotNull
    public <PARSED> void registerParser(TypeRange<PARSED> typeRange, ArgumentKey key, Parser<SENDER, ?, PARSED> parser) {
        List<BucketByArgument<?>> arguments = buckets.computeIfAbsent(typeRange, () -> new BucketByArgument<>());

        for (BucketByArgument<?> argument : arguments) {
            BucketByArgument<PARSED> bucket = (BucketByArgument<PARSED>) argument;
            bucket.registerParser(typeRange, key, parser);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    @NotNull
    public <PARSED> ParserSet<SENDER, PARSED> getParserSet(Class<PARSED> parserType, ArgumentKey key) {
        return new MergedParserSetImpl<>(buckets.get(parserType));
    }

    class BucketByArgument<PARSED> extends BucketByArgumentUniversal<PARSED> {

        private final BucketByArgumentUniversal<PARSED> universalTypedBucket = new BucketByArgumentUniversal<>(true);

        private BucketByArgument() {
            super(false);
        }

        @Override
        void registerParser(TypeRange<PARSED> parsedType, ArgumentKey key, Parser<SENDER, ?, PARSED> parser) {
            if (key.isUniversal()) {
                this.universalTypedBucket.registerParser(parsedType, key, parser);
                return;
            }

            super.registerParser(parsedType, key, parser);
        }

        @Override
        @Nullable
        ParserSet<SENDER, PARSED> getParserSet(ArgumentKey key) {
            ParserSet<SENDER, PARSED> bucket = super.getParserSet(key);

            if (bucket != null) {
                return bucket;
            }

            ParserSet<SENDER, PARSED> set = universalTypedBucket.getParserSet(key);

            if (set != null) {
                return set;
            }

            return new EmptyParserSetImpl<>();
        }

    }

    private class BucketByArgumentUniversal<PARSED> {

        private final BiMap<String, String, ParserSetImpl<SENDER, PARSED>> buckets = new BiHashMap<>();
        private final boolean ignoreNamespace;

        private BucketByArgumentUniversal(boolean ignoreNamespace) {
            this.ignoreNamespace = ignoreNamespace;
        }

        void registerParser(TypeRange<PARSED> parsedType, ArgumentKey key, Parser<SENDER, ?, PARSED> parser) {
            ParserSetImpl<SENDER, PARSED> bucket = buckets.computeIfAbsent(key.getKey(), key.getNamespace(), (k1, k2) -> new ParserSetImpl<>(parsedType));

            bucket.registerParser(parser);
        }

        @Nullable
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
