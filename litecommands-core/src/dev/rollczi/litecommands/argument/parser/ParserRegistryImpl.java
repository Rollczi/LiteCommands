package dev.rollczi.litecommands.argument.parser;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.ArgumentKey;
import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.range.Range;
import dev.rollczi.litecommands.reflect.type.TypeRange;
import dev.rollczi.litecommands.shared.BiHashMap;
import dev.rollczi.litecommands.shared.BiMap;
import dev.rollczi.litecommands.reflect.type.TypeIndex;
import dev.rollczi.litecommands.util.StringUtil;
import dev.rollczi.litecommands.wrapper.WrapFormat;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ParserRegistryImpl<SENDER> implements ParserRegistry<SENDER>, ParserChainAccessor<SENDER> {

    private final TypeIndex<BucketByArgument<?>> buckets = new TypeIndex<>();

    @Override
    @SuppressWarnings("unchecked")
    public <PARSED> void registerParser(TypeRange<PARSED> typeRange, ArgumentKey key, Parser<SENDER, PARSED> parser) {
        List<BucketByArgument<?>> arguments = buckets.computeIfAbsent(typeRange, () -> new BucketByArgument<>());

        for (BucketByArgument<?> argument : arguments) {
            BucketByArgument<PARSED> bucket = (BucketByArgument<PARSED>) argument;
            bucket.registerParser(typeRange, key, parser);
        }
    }

    @Override
    public <PARSED> void registerParser(TypeRange<PARSED> typeRange, ArgumentKey key, ParserChained<SENDER, PARSED> parser) {
        this.registerParser(typeRange, key, new ChainRedirectParser<>(parser));
    }

    @Override
    @NotNull
    public <PARSED> ParserSet<SENDER, PARSED> getParserSet(Class<PARSED> parserType, ArgumentKey key) {
        List<ParserSet<SENDER, PARSED>> parserSets = new ArrayList<>();

        for (BucketByArgument<?> argument : buckets.get(parserType)) {
            BucketByArgument<PARSED> bucket = (BucketByArgument<PARSED>) argument;
            ParserSet<SENDER, PARSED> parserSet = bucket.getParserSet(key);

            if (parserSet != null) {
                parserSets.add(parserSet);
            }
        }

        return new MergedParserSetImpl<>(parserSets);
    }

    @Override
    public <PARSED> Parser<SENDER, PARSED> getParser(Invocation<SENDER> invocation, Argument<PARSED> argument) {
        Class<PARSED> argumentType = argument.getWrapperFormat().getParsedType();
        ParserSet<SENDER, PARSED> parserSet = getParserSet(argumentType, argument.getKey());

        return parserSet.getValidParserOrThrow(invocation, argument);
    }

    @Override
    public <T> ParseResult<T> parse(Invocation<SENDER> invocation, Argument<T> argument, RawInput input) {
        Parser<SENDER, T> parser = getParser(invocation, argument);

        return parser.parse(invocation, argument, input);
    }

    class BucketByArgument<PARSED> extends BucketByArgumentUniversal<PARSED> {

        private final BucketByArgumentUniversal<PARSED> universalTypedBucket = new BucketByArgumentUniversal<>(true);

        private BucketByArgument() {
            super(false);
        }

        @Override
        void registerParser(TypeRange<PARSED> parsedType, ArgumentKey key, Parser<SENDER, PARSED> parser) {
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

        void registerParser(TypeRange<PARSED> parsedType, ArgumentKey key, Parser<SENDER, PARSED> parser) {
            ParserSetImpl<SENDER, PARSED> bucket = buckets.computeIfAbsent(key.getKey(), key.getNamespace(), (k1, k2) -> new ParserSetImpl<>());

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

    private class ChainRedirectParser<PARSED> implements Parser<SENDER, PARSED> {

        private final ParserChained<SENDER, PARSED> parser;

        private ChainRedirectParser(ParserChained<SENDER, PARSED> parser) {
            this.parser = parser;
        }

        @Override
        public ParseResult<PARSED> parse(Invocation<SENDER> invocation, Argument<PARSED> argument, RawInput input) {
            return parser.parse(invocation, argument, input, ParserRegistryImpl.this);
        }

        @Override
        public boolean canParse(Invocation<SENDER> invocation, Argument<PARSED> argument) {
            return parser.canParse(invocation, argument);
        }

        @Override
        public Range getRange(Argument<PARSED> argument) {
            return parser.getRange(argument);
        }

    }

}
