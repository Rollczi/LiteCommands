package dev.rollczi.litecommands.argument.parser;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.ArgumentKey;
import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.range.Range;
import dev.rollczi.litecommands.reflect.type.TypeRange;
import dev.rollczi.litecommands.reflect.type.TypeIndex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public class ParserRegistryImpl<SENDER> implements ParserRegistry<SENDER>, ParserChainAccessor<SENDER> {

    private final Map<Argument<?>, Parser<SENDER, ?>> cachedParsers = new HashMap<>();
    private final TypeIndex<ParserNamespacedIndex<SENDER, ?>> typeIndex = new TypeIndex<>();

    @Override
    @SuppressWarnings("unchecked")
    public <T> void registerParser(TypeRange<T> typeRange, ArgumentKey key, Parser<SENDER, T> parser) {
        List<ParserNamespacedIndex<SENDER, ?>> indexList = typeIndex.computeIfAbsent(typeRange, () -> new ParserNamespacedIndex<>());

        for (ParserNamespacedIndex<SENDER, ?> index : indexList) {
            ParserNamespacedIndex<SENDER, T> typedIndex = (ParserNamespacedIndex<SENDER, T>) index;
            typedIndex.registerParser(key, parser);
        }
    }

    @Override
    public <T> void registerParser(TypeRange<T> typeRange, ArgumentKey key, ParserChained<SENDER, T> parser) {
        this.registerParser(typeRange, key, new ChainRedirectParser<>(this, parser));
    }

    @Override
    @NotNull
    @SuppressWarnings("unchecked")
    public <T> ParserSet<SENDER, T> getParserSet(Class<T> parserType, ArgumentKey key) {
        List<ParserSet<SENDER, T>> parserSets = new ArrayList<>();

        for (ParserNamespacedIndex<SENDER, ?> argument : typeIndex.get(parserType)) {
            ParserNamespacedIndex<SENDER, T> bucket = (ParserNamespacedIndex<SENDER, T>) argument;
            ParserSet<SENDER, T> parserSet = bucket.getParserSet(key);

            if (parserSet instanceof NamespacedParserSet) {
                NamespacedParserSet<SENDER, T> namespacedParserSet = (NamespacedParserSet<SENDER, T>) parserSet;

                if (namespacedParserSet.getNamespace().equals(key.getNamespace())) {
                    parserSets.add(0, namespacedParserSet);
                }
            }

            if (parserSet != null) {
                parserSets.add(parserSet);
            }
        }

        return new MergedParserSetImpl<>(parserSets);
    }

    @Override
    public <T> Parser<SENDER, T> getParser(Argument<T> argument) {
        Class<T> argumentType = argument.getType().getRawType();
        Parser<SENDER, T> senderParser = (Parser<SENDER, T>) cachedParsers.get(argument);

        if (senderParser == null) {
            ParserSet<SENDER, T> parserSet = getParserSet(argumentType, argument.getKey());
            senderParser = parserSet.getValidParserOrThrow(argument);
            cachedParsers.put(argument, senderParser);
        }

        return senderParser;
    }

    @Override
    public <T> Parser<SENDER, T> getParserOrNull(Argument<T> argument) {
        Class<T> argumentType = argument.getType().getRawType();
        Parser<SENDER, T> senderParser = (Parser<SENDER, T>) cachedParsers.get(argument);

        if (senderParser == null) {
            ParserSet<SENDER, T> parserSet = getParserSet(argumentType, argument.getKey());
            senderParser = parserSet.getValidParser(argument);

            if (senderParser != null) {
                cachedParsers.put(argument, senderParser);
            }
        }

        return senderParser;
    }

    @Override
    public <T> ParseResult<T> parse(Invocation<SENDER> invocation, Argument<T> argument, RawInput input) {
        Parser<SENDER, T> parser = getParser(argument);
        Range range = parser.getRange(argument);

        if (range.isInRangeOrAbove(input.size())) {
            return parser.parse(invocation, argument, input);
        }

        if (!input.hasNext()) {
            Optional<ParseResult<T>> optional = argument.getDefaultValue();

            return optional
                .orElseGet(() -> ParseResult.failure(InvalidUsage.Cause.MISSING_ARGUMENT));
        }

        return ParseResult.failure(InvalidUsage.Cause.MISSING_PART_OF_ARGUMENT);
    }

}
