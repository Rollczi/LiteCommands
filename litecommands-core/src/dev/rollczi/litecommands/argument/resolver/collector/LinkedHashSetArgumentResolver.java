package dev.rollczi.litecommands.argument.resolver.collector;

import dev.rollczi.litecommands.argument.parser.ParserRegistry;
import dev.rollczi.litecommands.argument.suggester.SuggesterRegistry;
import dev.rollczi.litecommands.invocation.Invocation;
import java.util.LinkedHashSet;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class LinkedHashSetArgumentResolver<SENDER> extends AbstractCollectorArgumentResolver<SENDER, LinkedHashSet> {

    public LinkedHashSetArgumentResolver(ParserRegistry<SENDER> parserRegistry, SuggesterRegistry<SENDER> suggesterRegistry) {
        super(parserRegistry, suggesterRegistry);
    }

    @Override
    <E> Collector<E, ?, ? extends LinkedHashSet> getCollector(VarargsProfile varargsProfile, Invocation<SENDER> invocation) {
        return Collectors.toCollection(() -> new LinkedHashSet<>());
    }

}

