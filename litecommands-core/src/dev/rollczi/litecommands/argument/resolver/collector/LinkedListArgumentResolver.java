package dev.rollczi.litecommands.argument.resolver.collector;

import dev.rollczi.litecommands.argument.parser.ParserRegistry;
import dev.rollczi.litecommands.argument.suggester.SuggesterRegistry;
import dev.rollczi.litecommands.invocation.Invocation;
import java.util.LinkedList;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class LinkedListArgumentResolver<SENDER> extends AbstractCollectorArgumentResolver<SENDER, LinkedList> {

    public LinkedListArgumentResolver(ParserRegistry<SENDER> parserRegistry, SuggesterRegistry<SENDER> suggesterRegistry) {
        super(parserRegistry, suggesterRegistry);
    }

    @Override
    <E> Collector<E, ?, ? extends LinkedList<E>> getCollector(VarargsProfile collectionArgument, Invocation<SENDER> invocation) {
        return Collectors.toCollection(() -> new LinkedList<>());
    }

}

