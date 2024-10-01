package dev.rollczi.litecommands.argument.resolver.collector;

import dev.rollczi.litecommands.argument.parser.ParserRegistry;
import dev.rollczi.litecommands.argument.suggester.SuggesterRegistry;
import dev.rollczi.litecommands.invocation.Invocation;
import java.util.ArrayList;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ArrayListArgumentResolver<SENDER> extends AbstractCollectorArgumentResolver<SENDER, ArrayList> {

    public ArrayListArgumentResolver(ParserRegistry<SENDER> parserRegistry, SuggesterRegistry<SENDER> suggesterRegistry) {
        super(parserRegistry, suggesterRegistry);
    }

    @Override
    <E> Collector<E, ?, ? extends ArrayList<E>> getCollector(CollectionArgumentProfile collectionArgument, Invocation<SENDER> invocation) {
        return Collectors.toCollection(() -> new ArrayList<>());
    }

}

