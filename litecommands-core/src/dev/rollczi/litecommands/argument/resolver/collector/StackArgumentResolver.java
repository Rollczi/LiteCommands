package dev.rollczi.litecommands.argument.resolver.collector;

import dev.rollczi.litecommands.argument.parser.ParserRegistry;
import dev.rollczi.litecommands.argument.suggester.SuggesterRegistry;
import dev.rollczi.litecommands.invocation.Invocation;
import java.util.Stack;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class StackArgumentResolver<SENDER> extends AbstractCollectorArgumentResolver<SENDER, Stack> {

    public StackArgumentResolver(ParserRegistry<SENDER> parserRegistry, SuggesterRegistry<SENDER> suggesterRegistry) {
        super(parserRegistry, suggesterRegistry);
    }

    @Override
    <E> Collector<E, ?, ? extends Stack<E>> getCollector(CollectionArgumentProfile collectionArgument, Invocation<SENDER> invocation) {
        return Collectors.toCollection(() -> new Stack<>());
    }

}

