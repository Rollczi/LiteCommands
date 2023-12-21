package dev.rollczi.litecommands.argument.resolver.collector;

import dev.rollczi.litecommands.argument.parser.ParserRegistry;
import dev.rollczi.litecommands.argument.suggester.SuggesterRegistry;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.reflect.type.TypeToken;
import java.util.ArrayList;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ArrayListArgumentResolver<SENDER> extends AbstractCollectorArgumentResolver<SENDER, Object, ArrayList> {

    public ArrayListArgumentResolver(ParserRegistry<SENDER> parserRegistry, SuggesterRegistry<SENDER> suggesterRegistry) {
        super(parserRegistry, suggesterRegistry);
    }

    @Override
    Collector<Object, ?, ArrayList<Object>> getCollector(CollectorArgument<ArrayList> collectorArgument, Invocation<SENDER> invocation) {
        return Collectors.toCollection(ArrayList::new);
    }

    @Override
    protected Class<Object> getElementType(CollectorArgument<ArrayList> context, Invocation<SENDER> invocation) {
        TypeToken<?> elementType =  context.getWrapperFormat().parsedType().getParameterized();

        return (Class<Object>) elementType.getRawType();
    }

}

