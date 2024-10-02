package dev.rollczi.litecommands.argument.parser;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.ArgumentKey;
import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.reflect.type.TypeRange;
import dev.rollczi.litecommands.reflect.type.TypeIndex;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class ParserRegistryImpl<SENDER> implements ParserRegistry<SENDER>, ParserChainAccessor<SENDER> {

    private final TypeIndex<ParserNamespacedIndex<SENDER, ?>> typeIndex = new TypeIndex<>();

    @Override
    @SuppressWarnings("unchecked")
    public <T> void registerParser(TypeRange<T> typeRange, ArgumentKey key, Parser<SENDER, T> parser) {
        List<ParserNamespacedIndex<SENDER, ?>> arguments = typeIndex.computeIfAbsent(typeRange, () -> new ParserNamespacedIndex<>());

        for (ParserNamespacedIndex<SENDER, ?> argument : arguments) {
            ParserNamespacedIndex<SENDER, T> bucket = (ParserNamespacedIndex<SENDER, T>) argument;
            bucket.registerParser(key, parser);
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
        ParserSet<SENDER, T> parserSet = getParserSet(argumentType, argument.getKey());

        return parserSet.getValidParserOrThrow(argument);
    }

    @Override
    public <T> Parser<SENDER, T> getParserOrNull(Argument<T> argument) {
        Class<T> argumentType = argument.getType().getRawType();
        ParserSet<SENDER, T> parserSet = getParserSet(argumentType, argument.getKey());

        return parserSet.getValidParser(argument);
    }

    @Override
    public <T> ParseResult<T> parse(Invocation<SENDER> invocation, Argument<T> argument, RawInput input) {
        Parser<SENDER, T> parser = getParser(argument);

        return parser.parse(invocation, argument, input);
    }

}
