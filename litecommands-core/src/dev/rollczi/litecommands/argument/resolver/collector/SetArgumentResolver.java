package dev.rollczi.litecommands.argument.resolver.collector;

import dev.rollczi.litecommands.argument.parser.ParserRegistry;
import dev.rollczi.litecommands.argument.suggester.SuggesterRegistry;
import dev.rollczi.litecommands.invocation.Invocation;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class SetArgumentResolver<SENDER> extends AbstractCollectorArgumentResolver<SENDER, Set> {

    public SetArgumentResolver(ParserRegistry<SENDER> parserRegistry, SuggesterRegistry<SENDER> suggesterRegistry) {
        super(parserRegistry, suggesterRegistry);
    }

    @Override
    <E> Collector<E, ?, ? extends Set<E>> getCollector(VarargsProfile varargsProfile, Invocation<SENDER> invocation) {
        return Collectors.toSet();
    }

}

