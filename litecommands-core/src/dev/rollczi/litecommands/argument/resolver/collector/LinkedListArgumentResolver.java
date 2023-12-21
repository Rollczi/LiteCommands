package dev.rollczi.litecommands.argument.resolver.collector;

import dev.rollczi.litecommands.argument.parser.ParserRegistry;
import dev.rollczi.litecommands.argument.suggester.SuggesterRegistry;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.reflect.type.TypeToken;
import java.util.LinkedList;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class LinkedListArgumentResolver<SENDER> extends AbstractCollectorArgumentResolver<SENDER, Object, LinkedList> {

    public LinkedListArgumentResolver(ParserRegistry<SENDER> parserRegistry, SuggesterRegistry<SENDER> suggesterRegistry) {
        super(parserRegistry, suggesterRegistry);
    }

    @Override
    Collector<Object, ?, LinkedList<Object>> getCollector(CollectorArgument<LinkedList> collectorArgument, Invocation<SENDER> invocation) {
        return Collectors.toCollection(() -> new LinkedList<>());
    }

    @Override
    protected Class<Object> getElementType(CollectorArgument<LinkedList> context, Invocation<SENDER> invocation) {
        TypeToken<?> elementType =  context.getWrapperFormat().parsedType().getParameterized();

        return (Class<Object>) elementType.getRawType();
    }

}

