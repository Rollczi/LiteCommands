package dev.rollczi.litecommands.argument.resolver.collector;

import dev.rollczi.litecommands.argument.parser.ParserRegistry;
import dev.rollczi.litecommands.argument.suggester.SuggesterRegistry;
import dev.rollczi.litecommands.invocation.Invocation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class CollectionArgumentResolver<SENDER> extends AbstractCollectorArgumentResolver<SENDER, Collection> {

    public CollectionArgumentResolver(ParserRegistry<SENDER> parserRegistry, SuggesterRegistry<SENDER> suggesterRegistry) {
        super(parserRegistry, suggesterRegistry);
    }

    @Override
    <E> Collector<E, ?, ? extends ArrayList<E>> getCollector(VarargsProfile varargsProfile, Invocation<SENDER> invocation) {
        return Collectors.toCollection(() -> new ArrayList<>());
    }

}

