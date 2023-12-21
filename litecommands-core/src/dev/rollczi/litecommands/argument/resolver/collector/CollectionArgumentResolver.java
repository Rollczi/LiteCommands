package dev.rollczi.litecommands.argument.resolver.collector;

import dev.rollczi.litecommands.argument.parser.ParserRegistry;
import dev.rollczi.litecommands.argument.suggester.SuggesterRegistry;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.reflect.type.TypeToken;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class CollectionArgumentResolver<SENDER> extends AbstractCollectorArgumentResolver<SENDER, Object, Collection> {

    public CollectionArgumentResolver(ParserRegistry<SENDER> parserRegistry, SuggesterRegistry<SENDER> suggesterRegistry) {
        super(parserRegistry, suggesterRegistry);
    }

    @Override
    Collector<Object, ?, Collection<Object>> getCollector(CollectorArgument<Collection> collectorArgument, Invocation<SENDER> invocation) {
        return Collectors.toCollection(() -> new ArrayList<>());
    }

    @Override
    protected Class<Object> getElementType(CollectorArgument<Collection> context, Invocation<SENDER> invocation) {
        TypeToken<?> elementType =  context.getWrapperFormat().parsedType().getParameterized();

        return (Class<Object>) elementType.getRawType();
    }

}

