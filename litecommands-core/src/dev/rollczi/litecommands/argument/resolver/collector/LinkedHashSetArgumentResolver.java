package dev.rollczi.litecommands.argument.resolver.collector;

import dev.rollczi.litecommands.argument.parser.ParserRegistry;
import dev.rollczi.litecommands.argument.suggester.SuggesterRegistry;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.reflect.type.TypeToken;
import java.util.LinkedHashSet;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class LinkedHashSetArgumentResolver<SENDER> extends AbstractCollectorArgumentResolver<SENDER, Object, LinkedHashSet> {

    public LinkedHashSetArgumentResolver(ParserRegistry<SENDER> parserRegistry, SuggesterRegistry<SENDER> suggesterRegistry) {
        super(parserRegistry, suggesterRegistry);
    }

    @Override
    Collector<Object, ?, LinkedHashSet<Object>> getCollector(CollectorArgument<LinkedHashSet> collectorArgument, Invocation<SENDER> invocation) {
        return Collectors.toCollection(() -> new LinkedHashSet<>());
    }

    @Override
    protected Class<Object> getElementType(CollectorArgument<LinkedHashSet> context, Invocation<SENDER> invocation) {
        TypeToken<?> elementType =  context.getWrapperFormat().parsedType().getParameterized();

        return (Class<Object>) elementType.getRawType();
    }

}

